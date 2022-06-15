package com.duckcatchandfit.game.models;

import java.util.Date;
import java.util.List;

public interface IActivityReading {
    boolean isFullFilled();

    Date getStartDate();

    Date getEndDate();

    int getAccelerometerAccuracy();

    List<Float> getAccelerometerX();

    List<Float> getAccelerometerY();

    List<Float> getAccelerometerZ();

    int getGyroscopeAccuracy();

    List<Float> getGyroscopeX();

    List<Float> getGyroscopeY();

    List<Float> getGyroscopeZ();

    int getMagneticFieldAccuracy();

    List<Float> getOrientationAngleX();

    List<Float> getOrientationAngleY();

    List<Float> getOrientationAngleZ();

    String getActivity();

    String getDeviceId();

    int getInstanceSize();
}
