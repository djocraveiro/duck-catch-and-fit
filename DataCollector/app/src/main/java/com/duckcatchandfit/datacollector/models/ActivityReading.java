package com.duckcatchandfit.datacollector.models;

import android.annotation.SuppressLint;
import com.duckcatchandfit.datacollector.storage.ICsvData;

import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityReading implements ICsvData {

    //#region Constants

    public static final String JUMP_LEFT = "jump_left";
    public static final String FAKE_JUMP_LEFT = "fake_jump_left";
    public static final String JUMP_RIGHT = "jump_right";
    public static final String FAKE_JUMP_RIGHT = "fake_jump_right";
    public static final String OTHER = "other";

    //#endregion

    //#region Fields

    private Date startDate;
    private Date endDate;
    private int accelerometerAccuracy;
    private final List<Float> accelerometerX;
    private final List<Float> accelerometerY;
    private final List<Float> accelerometerZ;
    private int gyroscopeAccuracy;
    private final List<Float> gyroscopeX;
    private final List<Float> gyroscopeY;
    private final List<Float> gyroscopeZ;
    private String activity;

    //#endregion

    //#region Initializers

    public ActivityReading(int instanceSize) {
        this.startDate = new Date();
        this.endDate = new Date();
        this.accelerometerAccuracy = 0;
        this.accelerometerX = new ArrayList<>(instanceSize);
        this.accelerometerY = new ArrayList<>(instanceSize);
        this.accelerometerZ = new ArrayList<>(instanceSize);
        this.gyroscopeAccuracy = 0;
        this.gyroscopeX = new ArrayList<>(instanceSize);
        this.gyroscopeY = new ArrayList<>(instanceSize);
        this.gyroscopeZ = new ArrayList<>(instanceSize);
        this.activity = "UNKNOWN";
    }

    //#endregion

    //#region Properties

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getAccelerometerAccuracy() { return accelerometerAccuracy; }
    public void setAccelerometerAccuracy(int accelerometerAccuracy) {
        this.accelerometerAccuracy = accelerometerAccuracy;
    }

    public List<Float> getAccelerometerX() { return accelerometerX; }
    public List<Float> getAccelerometerY() { return accelerometerY; }
    public List<Float> getAccelerometerZ() { return accelerometerZ; }

    public int getGyroscopeAccuracy() { return gyroscopeAccuracy; }
    public void setGyroscopeAccuracy(int gyroscopeAccuracy) {
        this.gyroscopeAccuracy = gyroscopeAccuracy;
    }

    public List<Float> getGyroscopeX() { return gyroscopeX; }
    public List<Float> getGyroscopeY() { return gyroscopeY; }
    public List<Float> getGyroscopeZ() { return gyroscopeZ; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    //#endregion

    //#region Public Methods

    //#region ICsvData Methods

    @Override
    public String toCsvHeader(String colSeparator) {
        return "startDate" + colSeparator +
            "endDate" + colSeparator +
            "accelerometer-accuracy" + colSeparator +
            "accelerometer-x" + colSeparator +
            "accelerometer-y" + colSeparator +
            "accelerometer-z" + colSeparator +
            "gyroscope-accuracy" + colSeparator +
            "gyroscope-x" + colSeparator +
            "gyroscope-y" + colSeparator +
            "gyroscope-z" + colSeparator +
            "activity";
    }

    @Override
    public String toCsvRow(String colSeparator) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat.format(startDate) + colSeparator +
            dateFormat.format(endDate) + colSeparator +
            accelerometerAccuracy + colSeparator +
            Arrays.toString(accelerometerX.toArray()) + colSeparator +
            Arrays.toString(accelerometerY.toArray()) + colSeparator +
            Arrays.toString(accelerometerZ.toArray()) + colSeparator +
            gyroscopeAccuracy + colSeparator +
            Arrays.toString(gyroscopeX.toArray()) + colSeparator +
            Arrays.toString(gyroscopeY.toArray()) + colSeparator +
            Arrays.toString(gyroscopeZ.toArray()) + colSeparator +
            activity;
    }

    //#endregion

    //#endregion
}
