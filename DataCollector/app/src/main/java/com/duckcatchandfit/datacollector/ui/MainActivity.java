package com.duckcatchandfit.datacollector.ui;

import android.hardware.Sensor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.duckcatchandfit.datacollector.services.ICollectListener;
import com.duckcatchandfit.datacollector.utils.ApplicationExecutors;
import com.duckcatchandfit.datacollector.BuildConfig;
import com.duckcatchandfit.datacollector.services.DataCollector;
import com.duckcatchandfit.datacollector.R;
import com.duckcatchandfit.datacollector.models.ActivityReading;
import com.duckcatchandfit.datacollector.storage.CsvStorage;
import com.duckcatchandfit.datacollector.storage.FileServer;
import com.duckcatchandfit.datacollector.utils.Device;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private final FileServer fileServer = new FileServer();
    private final DataCollector dataCollector = new DataCollector();
    private final CsvStorage csvExporter = new CsvStorage(getExportFileName());
    private final String deviceId = Device.getDeviceId();

    //#endregion

    //#region Public Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileServer.setHostAddress(BuildConfig.SFTP_HOST);
        fileServer.setUsername(BuildConfig.SFTP_USR);
        fileServer.setPassword(BuildConfig.SFTP_PWD);
        fileServer.setRemoteDirectory(BuildConfig.SFTP_DIR);

        showMessage(getString(R.string.press_start_message));

        //debugSensorReadings(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        super.onPause();
    }

    //#region Listeners

    public void onStartClick(View v) {
        setActivityRecording(true);

        dataCollector.startReadingInstance();
        showMessage(getString(R.string.report_movment_message));
    }

    public void onJumpLeftClick(View v) {
        handleMovement(ActivityReading.JUMP_LEFT);
    }

    public void onFakeJumpLeftClick(View v) {
        handleMovement(ActivityReading.FAKE_JUMP_LEFT);
    }

    public void onJumpRightClick(View v) {
        handleMovement(ActivityReading.JUMP_RIGHT);
    }

    public void onFakeJumpRightClick(View v) {
        handleMovement(ActivityReading.FAKE_JUMP_RIGHT);
    }

    public void onStayClick(View v) {
        handleMovement(ActivityReading.STAY);
    }

    public void onOtherClick(View v) {
        handleMovement(ActivityReading.OTHER);
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

    private void handleMovement(String label) {
        setActivityRecording(false);

        dataCollector.stopReadingInstance();

        ActivityReading reading = dataCollector.getReading();
        reading.setActivity(label);
        reading.setDeviceId(deviceId);

        if (!csvExporter.hasHeader()) {
            csvExporter.writeHeader(this, reading);
        }

        csvExporter.writeToFile(this, reading);

        showMessage(getString(R.string.press_start_message));
    }

    private void showMessage(String message) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    private void setActivityRecording(boolean recording) {
        int startVisibility = recording
                ? View.INVISIBLE
                : View.VISIBLE;

        findViewById(R.id.buttonStart).setVisibility(startVisibility);
        findViewById(R.id.buttonStartUpload).setVisibility(startVisibility);

        int movementsVisibility = recording
                ? View.VISIBLE
                : View.INVISIBLE;

        findViewById(R.id.buttonJumpLeft).setVisibility(movementsVisibility);
        findViewById(R.id.buttonJumpRight).setVisibility(movementsVisibility);
        findViewById(R.id.buttonFakeJumpLeft).setVisibility(movementsVisibility);
        findViewById(R.id.buttonFakeJumpRight).setVisibility(movementsVisibility);
        findViewById(R.id.buttonStay).setVisibility(movementsVisibility);
        findViewById(R.id.buttonOther).setVisibility(movementsVisibility);
    }

    private void debugSensorReadings(int debugSensorType) {
        dataCollector.setCollectListener(new ICollectListener() {
            @Override
            public void onChange(int sensorType, float[] data) {
                if (sensorType == debugSensorType) {
                    Toast.makeText(MainActivity.this, Arrays.toString(data), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static String getExportFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        return dateFormat.format(new Date()) + "-" + Device.getDeviceId() + ".csv";
    }

    //#endregion

}