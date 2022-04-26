package com.duckcatchandfit.datacollector.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.duckcatchandfit.datacollector.BuildConfig;
import com.duckcatchandfit.datacollector.R;
import com.duckcatchandfit.datacollector.models.ActivityReading;
import com.duckcatchandfit.datacollector.services.DataCollector;
import com.duckcatchandfit.datacollector.services.ICollectListener;
import com.duckcatchandfit.datacollector.storage.CsvStorage;
import com.duckcatchandfit.datacollector.storage.FileServer;
import com.duckcatchandfit.datacollector.utils.ApplicationExecutors;
import com.duckcatchandfit.datacollector.utils.Device;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CollectModeActivity extends AppCompatActivity implements ICollectListener {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private final FileServer fileServer = new FileServer();
    private final DataCollector dataCollector = new DataCollector();
    private final CsvStorage csvExporter = new CsvStorage(getExportFileName());
    private final String deviceId = Device.getDeviceId();
    private boolean isRecording = false;

    //#endregion

    //#region Public Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_mode_activity);

        fileServer.setHostAddress(BuildConfig.SFTP_HOST);
        fileServer.setUsername(BuildConfig.SFTP_USR);
        fileServer.setPassword(BuildConfig.SFTP_PWD);
        fileServer.setRemoteDirectory(BuildConfig.SFTP_DIR);
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
            dataCollector.startReadingInstance(getCollectMode());
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

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setEnabled(!isRecording);

        Button startButton = findViewById(R.id.buttonStart);
        startButton.setText(isRecording ? R.string.stop : R.string.start);

        Button uploadButton = findViewById(R.id.buttonStartUpload);
        uploadButton.setEnabled(!isRecording);
    }

    private static String getExportFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        return dateFormat.format(new Date()) + "-" + Device.getDeviceId() + ".csv";
    }

    private int getCollectMode() {
        String activityLabel = getActivityLabel();

        if (activityLabel.equals(ActivityReading.STAYING)) {
            return DataCollector.COLLECT_MODE_INSTANCE_LOOP;
        }
        else {
            return DataCollector.COLLECT_MODE_SINGLE_INSTANCE;
        }
    }

    private String getActivityLabel() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId == R.id.radioButtonStaying) {
            return ActivityReading.STAYING;
        }
        else if (checkedId == R.id.radioButtonJumpLeft) {
            return ActivityReading.JUMP_LEFT;
        }
        else if (checkedId == R.id.radioButtonJumpRight) {
            return ActivityReading.JUMP_RIGHT;
        }
        else if (checkedId == R.id.radioButtonOther) {
            return ActivityReading.OTHER;
        }
        else if (checkedId == R.id.radioButtonFakeJumpLeft) {
            return ActivityReading.FAKE_JUMP_LEFT;
        }
        else if (checkedId == R.id.radioButtonFakeJumpRight) {
            return ActivityReading.FAKE_JUMP_RIGHT;
        }

        return "ERROR";
    }

    //#region ICollectListener

    @Override
    public void onCollectStop() {
        setActivityRecording(false);
    }

    @Override
    public void onInstanceCollected(final ActivityReading reading) {
        reading.setDeviceId(deviceId);
        reading.setActivity(getActivityLabel());

        if (!csvExporter.hasHeader()) {
            csvExporter.writeHeader(this, reading);
        }

        csvExporter.writeToFile(this, reading);
    }

    //#endregion

    //#endregion

}