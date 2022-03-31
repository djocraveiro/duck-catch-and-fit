package com.duckcatchandfit.datacollector;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private final FileServer fileServer = new FileServer();
    private final DataCollector dataCollector = new DataCollector();
    private final CsvExporter csvExporter = new CsvExporter(getExportFileName());

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataCollector.startListeningSensors(this);

        if (csvExporter.requestPermissions(this)) {
            Toast.makeText(this, getString(R.string.ready), Toast.LENGTH_SHORT).show();
            csvExporter.writeHeader(this, dataCollector.getReading());
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
        handleMovement(HumanActivityReading.JUMP_LEFT);
    }

    public void onJumpRightClick(View v) {
        handleMovement(HumanActivityReading.JUMP_RIGHT);
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

        HumanActivityReading reading = dataCollector.getReading();
        reading.setActivity(label);

        csvExporter.writeToFile(this, reading);
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

        int movementsVisibility = recording
                ? View.VISIBLE
                : View.INVISIBLE;

        findViewById(R.id.buttonJumpLeft).setVisibility(movementsVisibility);
        findViewById(R.id.buttonJumpRight).setVisibility(movementsVisibility);
    }

    private static String getExportFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(new Date()) + ".csv";
    }

    //#endregion

}