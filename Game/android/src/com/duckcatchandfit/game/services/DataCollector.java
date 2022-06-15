package com.duckcatchandfit.game.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.duckcatchandfit.game.models.ActivityReading;
import com.duckcatchandfit.game.utils.ApplicationExecutors;

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

    //#region Constants

    public static final int COLLECT_MODE_SINGLE_INSTANCE = 1;
    public static final int COLLECT_MODE_INSTANCE_LOOP = 2;

    private static final int INSTANCE_SIZE = 64;
    private static final int INSTANCE_OVERLAP_SIZE = 32;
    private static final int COLLECT_INTERVAL = 20; // milliseconds

    //#endregion

    //#region Fields

    private SensorManager sensorManager;
    private ActivityReading reading;
    private final float[] gravity = new float[3];
    private final float[] acceleration = new float[3];
    private final float[] rotationCurrent = new float[3];
    private final float[] orientationAngles = new float[3];
    private int accelerometerAccuracy;
    private int gyroscopeAccuracy;
    private int magneticFieldAccuracy;

    private ICollectListener collectListener = null;
    private final ApplicationExecutors exec = new ApplicationExecutors();
    private Timer timer;
    private int collectMode;
    private boolean isCollecting;

    //#endregion

    //#region Properties

    public void setCollectListener(ICollectListener collectListener) { this.collectListener = collectListener; }

    //#endregion

    //#region Public Methods

    public void startListeningSensors(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        }

        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        registerSensor(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    public void stopListeningSensors(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        }

        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        unregisterSensor(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    public void startReadingInstance(final int collectMode) {
        this.isCollecting = true;
        this.collectMode = collectMode;

        startNewInstance();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                exec.getBackground().execute(() -> {
                    Log.d("DataCollector", "collect-task-running");

                    copyDataToInstance();

                    if (reading.isFullFilled()) {
                        reading.setEndDate(new Date());
                        collectListener.onInstanceCollected(reading);

                        if (collectMode == COLLECT_MODE_SINGLE_INSTANCE) {
                            isCollecting = false;
                        }

                        if (!isCollecting) {
                            timer.cancel();
                            timer.purge();
                            timer = null;

                            exec.getMainThread().execute(() -> {
                                collectListener.onCollectStop();
                            });
                            return;
                        }

                        startNewInstance();
                    }
                });
            }
        },
        1, COLLECT_INTERVAL);
    }

    public void stopReadingInstance() {
        isCollecting = false;
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
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            handleMagneticFieldEvent(event);
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
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldAccuracy = accuracy;
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
                SensorManager.SENSOR_DELAY_GAME);
    }

    private  void unregisterSensor(Sensor sensor) {
        if (sensor == null) {
            return;
        }

        sensorManager.unregisterListener(this, sensor);
    }

    private void startNewInstance() {
        ActivityReading newReading = new ActivityReading(INSTANCE_SIZE);

        if (reading != null && collectMode == COLLECT_MODE_INSTANCE_LOOP) {
            final int toIndex = INSTANCE_SIZE - 1;
            reading.copyListsTo(newReading, toIndex - INSTANCE_OVERLAP_SIZE, toIndex);
        }

        reading = newReading;
        reading.setStartDate(new Date());
    }

    private void copyDataToInstance() {
        reading.setAccelerometerAccuracy(accelerometerAccuracy);
        reading.getAccelerometerX().add(acceleration[0]);
        reading.getAccelerometerY().add(acceleration[1]);
        reading.getAccelerometerZ().add(acceleration[2]);

        reading.setGyroscopeAccuracy(gyroscopeAccuracy);
        reading.getGyroscopeX().add(deltaRotationVector[0]);
        reading.getGyroscopeY().add(deltaRotationVector[1]);
        reading.getGyroscopeZ().add(deltaRotationVector[2]);

        reading.setMagneticFieldAccuracy(magneticFieldAccuracy);
        reading.getOrientationAngleX().add(orientationAngles[0]);
        reading.getOrientationAngleY().add(orientationAngles[1]);
        reading.getOrientationAngleZ().add(orientationAngles[2]);
    }

    //#region Accelerometer

    private static final float NOISE = 0.5f;
    private static final float ALPHA = 0.8f;
    private final float[] linearAcceleration = new float[3];

    private void handleAccelerometerEvent(SensorEvent event) {

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linearAcceleration[0] = event.values[0] - gravity[0];
        linearAcceleration[1] = event.values[1] - gravity[1];
        linearAcceleration[2] = event.values[2] - gravity[2];

        // Update acceleration X, Y and Z
        for(int axis = 0; axis < 3; axis++) {

            // high-pass filter
            if (Math.abs(linearAcceleration[axis] - acceleration[axis]) > NOISE) {
                acceleration[axis] = linearAcceleration[axis];
            }
        }
    }

    //#endregion

    //#region Gyroscope

    private static final float NanoSecondsToSeconds = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    private  void handleGyroscopeEvent(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NanoSecondsToSeconds;

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
        // rotationCurrent = rotationCurrent[0] * deltaRotationMatrix[0];

        // Update rotation X, Y and Z
        for(int axis = 0; axis < 3; axis++) {
            float newRotation = rotationCurrent[axis] * deltaRotationMatrix[axis];

            // high-pass filter
            if (Math.abs(newRotation - rotationCurrent[axis]) > NOISE) {
                rotationCurrent[axis] = newRotation;
            }
        }
    }

    //#endregion

    //#region Magnetic Field

    private void handleMagneticFieldEvent(SensorEvent event) {
        final float[] rotationMatrix = new float[9];

        SensorManager.getRotationMatrix(rotationMatrix, null, gravity, event.values);

        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

    //#endregion

    //#endregion

}
