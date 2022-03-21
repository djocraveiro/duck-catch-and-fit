package com.duckcatchandfit.datacollector;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //#region Fields

    private final DataCollector dataCollector = new DataCollector();
    private  final CsvExporter csvExporter = new CsvExporter("test_file.csv");

    //#endregion

    //#region Public Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMessage(getString(R.string.press_start_message));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (csvExporter.requestPermissions(this)) {
            Toast.makeText(this, getString(R.string.ready), Toast.LENGTH_SHORT).show();
            csvExporter.writeHeader(this, dataCollector.getReading());
        }
        else {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
        }
    }

    //#region Listeners

    public void onStartClick(View v) {
        setActivityRecording(true);

        dataCollector.start(this);
        showMessage(getString(R.string.report_movment_message));
    }

    public void onJumpLeftClick(View v) {
        handleMovement(HumanActivityReading.JUMP_LEFT);
    }

    public void onJumpRightClick(View v) {
        handleMovement(HumanActivityReading.JUMP_RIGHT);
    }

    //#endregion

    //#endregion

    //#region Private Methods

    private void handleMovement(String label) {
        setActivityRecording(false);

        dataCollector.stop();

        HumanActivityReading reading = dataCollector.getReading();
        reading.setLabel(label);

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

    //#endregion

}