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

import java.io.IOException;
import java.io.InputStream;
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
    private boolean isRecording = false;
    private final ActivityReading[] readingBuffer = new ActivityReading[10];

    private Classifier classifier;

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
        final ActivityReading lastActivity = readingBuffer[readingBuffer.length - 1];

        if (lastActivity != null && lastActivity.getActivity().equals(reading.getActivity())) {
            return;
        }

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

    private String getActivityLabel() {

        int checkedId = randomInt(1, 6);

        if (checkedId == 1) {
            return ActivityReading.JUMP_LEFT;
        }
        else if (checkedId == 2) {
            return ActivityReading.JUMP_RIGHT;
        }

        return ActivityReading.STAYING;
    }

    public static int randomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
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
        reading.setActivity(getActivityLabel());

        exec.getMainThread().execute(() -> logActivityPrediction(reading));

        if (!csvExporter.hasHeader()) {
            csvExporter.writeHeader(this, reading);
        }

        csvExporter.writeToFile(this, reading);
    }

    //#endregion

    //#endregion

    //#region Private Methods

    private Classifier loadModel() {
        InputStream inputStream = null;
        try {
            // deserialize model
            inputStream = getAssets().open("randomFores-test01.model");
            return (Classifier) weka.core.SerializationHelper.read(inputStream);
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_loading_model), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private String predictClass(final ActivityReading reading) {
        final Instance unlabeledInstance = FeatureExtractor.toInstance(reading);

        try {
            double prediction = classifier.classifyInstance(unlabeledInstance);

            return String.valueOf(prediction);
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.prediction_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

            return "";
        }
    }

    //#endregion
}