package com.duckcatchandfit.datacollector.ui;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.duckcatchandfit.datacollector.R;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class MainActivity extends AppCompatActivity {

    //#region Public Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCollectModeClick(View v) {
        final Intent intent = new Intent(this, CollectModeActivity.class);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }

    public void onTestModeClick(View v) {
        final Intent intent = new Intent(this, TestModeActivity.class);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }

    //#endregion
}