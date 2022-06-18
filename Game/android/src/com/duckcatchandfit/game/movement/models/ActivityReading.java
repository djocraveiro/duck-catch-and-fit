package com.duckcatchandfit.game.movement.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityReading implements IActivityReading {

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

    @Override
    public boolean isFullFilled() { return accelerometerX.size() == instanceSize; }

    @Override
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    @Override
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public int getAccelerometerAccuracy() { return accelerometerAccuracy; }
    public void setAccelerometerAccuracy(int accelerometerAccuracy) {
        this.accelerometerAccuracy = accelerometerAccuracy;
    }

    @Override
    public List<Float> getAccelerometerX() { return accelerometerX; }
    @Override
    public List<Float> getAccelerometerY() { return accelerometerY; }
    @Override
    public List<Float> getAccelerometerZ() { return accelerometerZ; }

    @Override
    public int getGyroscopeAccuracy() { return gyroscopeAccuracy; }
    public void setGyroscopeAccuracy(int gyroscopeAccuracy) {
        this.gyroscopeAccuracy = gyroscopeAccuracy;
    }

    @Override
    public List<Float> getGyroscopeX() { return gyroscopeX; }
    @Override
    public List<Float> getGyroscopeY() { return gyroscopeY; }
    @Override
    public List<Float> getGyroscopeZ() { return gyroscopeZ; }

    @Override
    public int getMagneticFieldAccuracy() { return magneticFieldAccuracy; }
    public void setMagneticFieldAccuracy(int magneticFieldAccuracy) {
        this.magneticFieldAccuracy = magneticFieldAccuracy;
    }

    @Override
    public List<Float> getOrientationAngleX() { return orientationAngleX; }
    @Override
    public List<Float> getOrientationAngleY() { return orientationAngleY; }
    @Override
    public List<Float> getOrientationAngleZ() { return orientationAngleZ; }

    @Override
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    @Override
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) {this.deviceId = deviceId; }

    @Override
    public int getInstanceSize() { return instanceSize; }

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

    //#endregion


}
