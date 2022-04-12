package com.duckcatchandfit.datacollector.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
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
    private boolean isRecording = false;

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
        setActivityRecording(!isRecording);

        if (isRecording){
            dataCollector.startReadingInstance();

            showMessage(getString(R.string.press_stop_message));
        }
        else {
            dataCollector.stopReadingInstance();

            reportMovement();

            showMessage(getString(R.string.press_start_message));
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

    private void reportMovement() {
        String[] activities = new String[] {
            ActivityReading.JUMP_LEFT,
            ActivityReading.JUMP_RIGHT,
            ActivityReading.STAY,
            ActivityReading.FAKE_JUMP_LEFT,
            ActivityReading.FAKE_JUMP_RIGHT,
            ActivityReading.OTHER
        };

        final ActivityReading reading = dataCollector.getReading();
        reading.setDeviceId(deviceId);

        new AlertDialog.Builder(this)
            .setTitle(R.string.report_movement_message)
            .setCancelable(false)
            .setItems(activities, (dialog, selectedIndex) -> {
                dialog.dismiss();

                reading.setActivity(activities[selectedIndex]);

                if (!csvExporter.hasHeader()) {
                    csvExporter.writeHeader(this, reading);
                }

                csvExporter.writeToFile(this, reading);
            })
            .show();
    }

    private void showMessage(String message) {
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    private void setActivityRecording(boolean recording) {
        isRecording = recording;

        int startVisibility = isRecording
                ? View.INVISIBLE
                : View.VISIBLE;

        Button startButton = findViewById(R.id.buttonStart);
        startButton.setText(isRecording ? R.string.stop : R.string.start);

        findViewById(R.id.buttonStart).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonStartUpload).setVisibility(startVisibility);
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