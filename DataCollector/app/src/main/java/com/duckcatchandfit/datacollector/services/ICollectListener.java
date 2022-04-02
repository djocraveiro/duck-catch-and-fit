package com.duckcatchandfit.datacollector.services;

public interface ICollectListener {
    void onChange(int sensorType, float[] data);
}
