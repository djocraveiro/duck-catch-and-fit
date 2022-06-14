package com.duckcatchandfit.datacollector.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.duckcatchandfit.datacollector.BuildConfig;
import com.duckcatchandfit.datacollector.R;
import com.duckcatchandfit.datacollector.models.ActivityReading;
import com.duckcatchandfit.datacollector.storage.ActivityReadingCsvAdapter;
import com.duckcatchandfit.datacollector.services.DataCollector;
import com.duckcatchandfit.datacollector.services.FeatureExtractor;
import com.duckcatchandfit.datacollector.services.ICollectListener;
import com.duckcatchandfit.datacollector.storage.CsvStorage;
import com.duckcatchandfit.datacollector.storage.FileServer;
import com.duckcatchandfit.datacollector.utils.ApplicationExecutors;
import com.duckcatchandfit.datacollector.utils.DateTime;
import com.duckcatchandfit.datacollector.utils.Device;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestModeActivity extends AppCompatActivity implements ICollectListener {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private final FileServer fileServer = new FileServer();
    private final DataCollector dataCollector = new DataCollector();
    private final CsvStorage csvExporter = new CsvStorage(getExportFileName());
    private final String deviceId = Device.getDeviceId();
    private ActivityReadingCsvAdapter readingCsvAdapter = null;
    private boolean isRecording = false;
    private final ActivityReading[] readingBuffer = new ActivityReading[10];
    private final FeatureExtractor featureExtractor = new FeatureExtractor();

    private Classifier classifier;
    private Instances dataSet;
    private long lastLateralPrediction = 0;

    //#endregion

    //#region Public Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_mode_activity);

        fileServer.setHostAddress(BuildConfig.SFTP_HOST);
        fileServer.setUsername(BuildConfig.SFTP_USR);
        fileServer.setPassword(BuildConfig.SFTP_PWD);
        fileServer.setRemoteDirectory(BuildConfig.SFTP_DIR);

        classifier = loadModel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataCollector.setCollectListener(this);
        dataCollector.startListeningSensors(this);

        if (csvExporter.requestPermissions(this)) {
            Toast.makeText(this, getString(R.string.ready), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        dataCollector.stopListeningSensors(this);
        dataCollector.setCollectListener(null);

        super.onPause();
    }

    //#region Listeners

    public void onStartClick(View v) {
        if (!isRecording){
            setActivityRecording(true);
            dataCollector.startReadingInstance(DataCollector.COLLECT_MODE_INSTANCE_LOOP);
        }
        else {
            dataCollector.stopReadingInstance();
        }
    }

    public void onUploadClick(View v) {
        exec.getBackground().execute(() -> {
            String filePath = getFilesDir() + "/" + csvExporter.getFileName();
            boolean success = fileServer.uploadFile(filePath);

            exec.getMainThread().execute(() -> {
                Toast.makeText(this, "Upload Data: " + success, Toast.LENGTH_SHORT).show();
            });
        });
    }

    //#endregion

    //#endregion

    //#region Private Methods

    private void setActivityRecording(boolean recording) {
        isRecording = recording;

        Button startButton = findViewById(R.id.buttonStart);
        startButton.setText(isRecording ? R.string.stop : R.string.start);

        Button uploadButton = findViewById(R.id.buttonStartUpload);
        uploadButton.setEnabled(!isRecording);
    }

    private static String getExportFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        return dateFormat.format(new Date()) + "-test-" + Device.getDeviceId() + ".csv";
    }

    private void logActivityPrediction(final ActivityReading reading) {
        final StringBuilder builder = new StringBuilder();
        final DateFormat dateFormat = DateTime.getLocalDateFormat();

        for(int i = 0; i < readingBuffer.length; i++) {
            if (i == readingBuffer.length - 1) {
                // add new activity at the last position
                readingBuffer[readingBuffer.length - 1] = reading;
            }
            else {
                // shift logs one position to the left
                readingBuffer[i] = readingBuffer[i + 1];
            }

            if (readingBuffer[i] != null) {
                builder.append(dateFormat.format(readingBuffer[i].getEndDate())).append(": ")
                        .append(readingBuffer[i].getActivity()).append("\n");
            }
        }

        final TextView textViewLog = findViewById(R.id.textViewLog);
        textViewLog.setText(builder.toString());
    }

    //#region ICollectListener

    @Override
    public void onCollectStop() {
        setActivityRecording(false);
    }

    @Override
    public void onInstanceCollected(final ActivityReading reading) {
        final String activityLabel = predictClass(reading);
        if (activityLabel.isEmpty()) {
            return;
        }

        reading.setDeviceId(deviceId);
        reading.setActivity(activityLabel);

        exec.getMainThread().execute(() -> logActivityPrediction(reading));

        //saveReading(reading);
    }

    //#endregion

    //#endregion

    //#region Private Methods

    private void saveReading(final ActivityReading reading) {
        if (readingCsvAdapter == null) {
            readingCsvAdapter = new ActivityReadingCsvAdapter(reading);
        }
        else {
            readingCsvAdapter.setActivityReading(reading);
        }

        if (!csvExporter.hasHeader()) {
            csvExporter.writeHeader(this, readingCsvAdapter);
        }

        csvExporter.writeToFile(this, readingCsvAdapter);
    }

    private Classifier loadModel() {
        Classifier classifier = null;

        try {
            final String modelFile = "randomForest.model";
            classifier = (Classifier) weka.core.SerializationHelper.read(getAssets().open(modelFile));

            final String datasetFile = "aux-dataset.arff";
            ConverterUtils.DataSource ds = new ConverterUtils.DataSource(getAssets().open(datasetFile));
            dataSet = ds.getDataSet();

            final int classIndex = dataSet.attribute("activity-class").index();
            dataSet.setClassIndex(classIndex);
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_loading_model), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return classifier;
    }

    private String predictClass(final ActivityReading reading) {
        final int SKIP_PREDICTION_MILLIS = 800;
        boolean skipPrediction = System.currentTimeMillis() - lastLateralPrediction < SKIP_PREDICTION_MILLIS;

        final Instance unlabeledInstance = featureExtractor.toInstance(reading);

        try {
            dataSet.clear();
            unlabeledInstance.setDataset(dataSet);

            double prediction = classifier.classifyInstance(unlabeledInstance);
            String label = dataSet.classAttribute().value((int)prediction);

            if (label.equals(ActivityReading.JUMP_LEFT) || label.equals(ActivityReading.JUMP_RIGHT)) {
                label = skipPrediction ? ("skip " + label) : label;
                lastLateralPrediction = System.currentTimeMillis();
            }

            return label;
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.prediction_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

            return "";
        }
    }

    //#endregion
}