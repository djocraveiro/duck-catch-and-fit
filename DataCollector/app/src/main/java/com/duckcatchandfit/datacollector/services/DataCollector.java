package com.duckcatchandfit.datacollector.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import com.duckcatchandfit.datacollector.utils.ApplicationExecutors;
import com.duckcatchandfit.datacollector.models.ActivityReading;

import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;
import static android.util.Half.EPSILON;
import static java.lang.Math.*;

/* Collects data for motion sensors
 * https://developer.android.com/guide/topics/sensors/sensors_motion
 */
public class DataCollector implements SensorEventListener {

    //#region Fields

    private static final int INSTANCE_SIZE = 50;
    private static final int COLLECT_INTERVAL = 20; // milliseconds

    private SensorManager sensorManager;
    private ActivityReading reading;
    private final float[] gravity = new float[3];
    private final float[] acceleration = new float[3];
    private final float[] deltaRotationVector = new float[4];
    private int accelerometerAccuracy;
    private int gyroscopeAccuracy;
    private float timestamp;

    private ICollectListener collectListener = null;
    private final ApplicationExecutors exec = new ApplicationExecutors();
    private final Timer timer = new Timer("data-collector");
    private final TimerTask collectTask = new TimerTask() {
        @Override
        public void run() {
            exec.getBackground().execute(() -> {
                reading.setAccelerometerAccuracy(accelerometerAccuracy);
                reading.getAccelerometerX().add(acceleration[0]);
                reading.getAccelerometerY().add(acceleration[1]);
                reading.getAccelerometerZ().add(acceleration[2]);

                reading.setGyroscopeAccuracy(gyroscopeAccuracy);
                reading.getGyroscopeX().add(deltaRotationVector[0]);
                reading.getGyroscopeY().add(deltaRotationVector[1]);
                reading.getGyroscopeZ().add(deltaRotationVector[2]);
            });
        }
    };

    //#endregion

    //#region Properties

    public ActivityReading getReading() {
        return reading;
    }

    public void setCollectListener(ICollectListener collectListener) { this.collectListener = collectListener; }

    //#endregion

    //#region Initializers

    public DataCollector() {
        this.reading = new ActivityReading(INSTANCE_SIZE);
    }

    //#endregion

    //#region Public Methods

    public void startListeningSensors(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        }

        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
    }

    public void stopListeningSensors(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        }

        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
    }

    public void startReadingInstance() {
        reading = new ActivityReading(INSTANCE_SIZE);
        reading.setStartDate(new Date());

        timer.schedule(collectTask, 1, COLLECT_INTERVAL);
    }

    public void stopReadingInstance() {
        reading.setEndDate(new Date());

        timer.cancel();
    }

    //#region SensorEventListener

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleAccelerometerEvent(event);
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            handleGyroscopeEvent(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == null) {
            return;
        }

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerAccuracy = accuracy;
        }
        else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeAccuracy = accuracy;
        }
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

    //#region Accelerometer

    private static final float NOISE = 2.0f;
    private static final float ALPHA = 0.8f;

    private void handleAccelerometerEvent(SensorEvent event) {
        final float[] linearAcceleration = new float[3];

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linearAcceleration[0] = event.values[0] - gravity[0];
        linearAcceleration[1] = event.values[1] - gravity[1];
        linearAcceleration[2] = event.values[2] - gravity[2];

        // Update acceleration X, Y and Z
        boolean changed = false;
        for(int axis = 0; axis < 3; axis++) {
            if (Math.abs(linearAcceleration[axis] - acceleration[axis]) > NOISE) {
                acceleration[axis] = linearAcceleration[axis];
                changed = true;
            }
        }

        if (changed && collectListener != null) {
            exec.getMainThread().execute(() -> {
                collectListener.onChange(Sensor.TYPE_ACCELEROMETER, acceleration);
            });
        }
    }

    //#endregion

    //#region Gyroscope

    private static final float NS2S = 1.0f / 1000000000.0f;

    private  void handleGyroscopeEvent(SensorEvent event) {
        //reading.getGyroscopeX().add(event.values[0]);
        //reading.getGyroscopeY().add(event.values[1]);
        //reading.getGyroscopeZ().add(event.values[2]);

        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;

            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) sqrt((axisX * axisX) + (axisY * axisY) + (axisZ * axisZ));

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) sin(thetaOverTwo);
            float cosThetaOverTwo = (float) cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
    }

    //#endregion

    //#endregion

}
