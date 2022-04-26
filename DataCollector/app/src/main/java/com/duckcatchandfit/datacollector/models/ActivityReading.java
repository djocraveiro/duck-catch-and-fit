package com.duckcatchandfit.datacollector.models;

import android.annotation.SuppressLint;
import com.duckcatchandfit.datacollector.storage.ICsvData;
import com.duckcatchandfit.datacollector.utils.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityReading implements ICsvData {

    //#region Constants

    public static final String JUMP_LEFT = "jump_left";
    public static final String JUMP_RIGHT = "jump_right";
    public static final String STAYING = "staying";
    public static final String FAKE_JUMP_LEFT = "fake_jump_left";
    public static final String FAKE_JUMP_RIGHT = "fake_jump_right";
    public static final String OTHER = "other";

    //#endregion

    //#region Fields

    private final int instanceSize;
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
    private int magneticFieldAccuracy;
    private final List<Float> orientationAngleX;
    private final List<Float> orientationAngleY;
    private final List<Float> orientationAngleZ;
    private String activity;
    private String deviceId;

    private final static FeatureStats stats = new FeatureStats();
    private final static SimpleDateFormat dateFormat = DateTime.getISO8601DateFormat();

    //#endregion

    //#region Initializers

    public ActivityReading(int instanceSize) {
        this.instanceSize = instanceSize;
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
        this.magneticFieldAccuracy = 0;
        this.orientationAngleX = new ArrayList<>(instanceSize);
        this.orientationAngleY = new ArrayList<>(instanceSize);
        this.orientationAngleZ = new ArrayList<>(instanceSize);
        this.activity = "UNKNOWN";
        this.deviceId = "UNKNOWN";
    }

    //#endregion

    //#region Properties

    public boolean isFullFilled() { return accelerometerX.size() == instanceSize; }

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

    public int getMagneticFieldAccuracy() { return magneticFieldAccuracy; }
    public void setMagneticFieldAccuracy(int magneticFieldAccuracy) {
        this.magneticFieldAccuracy = magneticFieldAccuracy;
    }

    public List<Float> getOrientationAngleX() { return orientationAngleX; }
    public List<Float> getOrientationAngleY() { return orientationAngleY; }
    public List<Float> getOrientationAngleZ() { return orientationAngleZ; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) {this.deviceId = deviceId; }

    //#endregion

    //#region Public Methods

    public void copyListsTo(ActivityReading reading, int fromIndex, int toIndex) {
        reading.accelerometerX.addAll(this.accelerometerX.subList(fromIndex, toIndex));
        reading.accelerometerY.addAll(this.accelerometerY.subList(fromIndex, toIndex));
        reading.accelerometerZ.addAll(this.accelerometerZ.subList(fromIndex, toIndex));

        reading.gyroscopeX.addAll(this.gyroscopeX.subList(fromIndex, toIndex));
        reading.gyroscopeY.addAll(this.gyroscopeY.subList(fromIndex, toIndex));
        reading.gyroscopeZ.addAll(this.gyroscopeZ.subList(fromIndex, toIndex));

        reading.orientationAngleX.addAll(this.orientationAngleX.subList(fromIndex, toIndex));
        reading.orientationAngleY.addAll(this.orientationAngleY.subList(fromIndex, toIndex));
        reading.orientationAngleZ.addAll(this.orientationAngleZ.subList(fromIndex, toIndex));
    }

    @Override
    public String toCsvHeader(String colSeparator) {
        StringBuilder builder = new StringBuilder();

        builder.append("startDate").append(colSeparator)
                .append("endDate").append(colSeparator);

        appendAccelerometerHeader(builder, colSeparator);
        appendGyroscopeHeader(builder, colSeparator);
        appendMagneticFieldHeader(builder, colSeparator);

        builder.append("activity").append(colSeparator)
            .append("device-id");

        return builder.toString();
    }

    @Override
    public String toCsvRow(String colSeparator) {
        StringBuilder builder = new StringBuilder();

        builder.append(dateFormat.format(startDate)).append(colSeparator)
            .append(dateFormat.format(endDate)).append(colSeparator);

        appendAccelerometerData(builder, colSeparator);
        appendGyroscopeData(builder, colSeparator);
        appendMagneticFieldData(builder, colSeparator);

        builder.append(activity).append(colSeparator)
            .append(deviceId);

        return builder.toString();
    }

    //#endregion

    //#region Private Methods

    private void appendAccelerometerHeader(StringBuilder builder, String colSeparator) {
        builder.append("acc-accuracy").append(colSeparator)
            .append("acc-x-m/s^2").append(colSeparator)
            .append("acc-y-m/s^2").append(colSeparator)
            .append("acc-z-m/s^2").append(colSeparator);

        builder.append(stats.setFeatureName("acc-x")
                .toCsvHeader(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("acc-y")
                .toCsvHeader(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("acc-z")
                .toCsvHeader(colSeparator)).append(colSeparator);
    }

    private void appendAccelerometerData(StringBuilder builder, String colSeparator) {
        builder.append(accelerometerAccuracy).append(colSeparator)
            .append(Arrays.toString(accelerometerX.toArray())).append(colSeparator)
            .append(Arrays.toString(accelerometerY.toArray())).append(colSeparator)
            .append(Arrays.toString(accelerometerZ.toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("acc-x")
                .calc(accelerometerX)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("acc-y")
                .calc(accelerometerY)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("acc-z")
                .calc(accelerometerZ)
                .toCsvRow(colSeparator)).append(colSeparator);
    }

    private void appendGyroscopeHeader(StringBuilder builder, String colSeparator) {
        builder.append("gyr-accuracy").append(colSeparator)
            .append("gyr-x-rad/s").append(colSeparator)
            .append("gyr-y-rad/s").append(colSeparator)
            .append("gyr-z-rad/s").append(colSeparator);

        builder.append(stats.setFeatureName("gyr-x")
                .toCsvHeader(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("gyr-y")
                .toCsvHeader(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("gyr-z")
                .toCsvHeader(colSeparator)).append(colSeparator);
    }

    private void appendGyroscopeData(StringBuilder builder, String colSeparator) {
        builder.append(gyroscopeAccuracy).append(colSeparator)
            .append(Arrays.toString(gyroscopeX.toArray())).append(colSeparator)
            .append(Arrays.toString(gyroscopeY.toArray())).append(colSeparator)
            .append(Arrays.toString(gyroscopeZ.toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("gyr-x")
                .calc(gyroscopeX)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("gyr-y")
                .calc(gyroscopeY)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("gyr-z")
                .calc(gyroscopeZ)
                .toCsvRow(colSeparator)).append(colSeparator);
    }

    private void appendMagneticFieldHeader(StringBuilder builder, String colSeparator) {
        builder.append("mag-accuracy").append(colSeparator)
                .append("ori-angle-x-rad").append(colSeparator)
                .append("ori-angle-y-rad").append(colSeparator)
                .append("ori-angle-z-rad").append(colSeparator);

        builder.append(stats.setFeatureName("ori-angle-x")
                .toCsvHeader(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("ori-angle-y")
                .toCsvHeader(colSeparator)).append(colSeparator)
        .append(stats.setFeatureName("ori-angle-z")
                .toCsvHeader(colSeparator)).append(colSeparator);
    }

    private void appendMagneticFieldData(StringBuilder builder, String colSeparator) {
        builder.append(magneticFieldAccuracy).append(colSeparator)
            .append(Arrays.toString(orientationAngleX.toArray())).append(colSeparator)
            .append(Arrays.toString(orientationAngleY.toArray())).append(colSeparator)
            .append(Arrays.toString(orientationAngleZ.toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("ori-angle-x")
                .calc(orientationAngleX)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("ori-angle-y")
                .calc(orientationAngleY)
                .toCsvRow(colSeparator)).append(colSeparator)
            .append(stats.setFeatureName("ori-angle-z")
                .calc(orientationAngleZ)
                .toCsvRow(colSeparator)).append(colSeparator);
    }

    //#endregion
}
