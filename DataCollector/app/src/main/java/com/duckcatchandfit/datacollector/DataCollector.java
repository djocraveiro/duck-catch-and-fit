package com.duckcatchandfit.datacollector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

/* Collects data for motion sensors
 * https://developer.android.com/guide/topics/sensors/sensors_motion
 */
public class DataCollector implements SensorEventListener {

    //#region Fields

    private static final int INSTANCE_SIZE = 50;

    private SensorManager sensorManager;
    private final HumanActivityReading reading;

    //#endregion

    //#region Properties

    public HumanActivityReading getReading() {
        return (HumanActivityReading)reading.clone();
    }

    //#endregion

    //#region Initializers

    public DataCollector() {
        reading = new HumanActivityReading(INSTANCE_SIZE);
    }

    //#endregion

    //#region Public Methods

    public void start(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        }

        reading.setStartDate(new Date());

        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
    }

    public void stop() {
        if (sensorManager == null) {
            return;
        }

        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));

        reading.setEndDate(new Date());
    }

    //#region SensorEventListener

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent == null) {
            return;
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            reading.getAccelerometerReadings().add(sensorEvent.values.clone());
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            reading.getGyroscopeReadings().add(sensorEvent.values.clone());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //#endregion

    //#endregion

    //#region Private Methods

    private void registerSensor(Sensor sensor) {
        if (sensor == null) {
            return;
        }

        sensorManager.registerListener(this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME);
    }

    private  void unregisterSensor(Sensor sensor) {
        if (sensor == null) {
            return;
        }

        sensorManager.unregisterListener(this, sensor);
    }

    //#endregion

}
