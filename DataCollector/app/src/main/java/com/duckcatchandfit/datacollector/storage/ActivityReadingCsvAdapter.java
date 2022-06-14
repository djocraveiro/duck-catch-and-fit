package com.duckcatchandfit.datacollector.storage;

import com.duckcatchandfit.datacollector.models.IActivityReading;
import com.duckcatchandfit.datacollector.utils.DateTime;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ActivityReadingCsvAdapter implements ICsvData {

    //#region Fields

    private IActivityReading reading;
    private final FeatureStats stats = new FeatureStats();
    private final SimpleDateFormat dateFormat = DateTime.getISO8601DateFormat();

    //#endregion

    //#region Properties

    public void setActivityReading(IActivityReading reading) {
        this.reading = reading;
    }

    //#endregion

    //#region Initializer

    public ActivityReadingCsvAdapter(IActivityReading activityReading) {
        this.reading = activityReading;
    }

    //#endregion

    //#region Public Methods

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

        builder.append(dateFormat.format(reading.getStartDate())).append(colSeparator)
                .append(dateFormat.format(reading.getEndDate())).append(colSeparator);

        appendAccelerometerData(builder, colSeparator);
        appendGyroscopeData(builder, colSeparator);
        appendMagneticFieldData(builder, colSeparator);

        builder.append(reading.getActivity()).append(colSeparator)
                .append(reading.getDeviceId());

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
        builder.append(reading.getAccelerometerAccuracy()).append(colSeparator)
                .append(Arrays.toString(reading.getAccelerometerX().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getAccelerometerY().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getAccelerometerZ().toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("acc-x")
                        .calc(reading.getAccelerometerX())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("acc-y")
                        .calc(reading.getAccelerometerY())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("acc-z")
                        .calc(reading.getAccelerometerZ())
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
        builder.append(reading.getGyroscopeAccuracy()).append(colSeparator)
                .append(Arrays.toString(reading.getGyroscopeX().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getGyroscopeY().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getGyroscopeZ().toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("gyr-x")
                        .calc(reading.getGyroscopeX())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("gyr-y")
                        .calc(reading.getGyroscopeY())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("gyr-z")
                        .calc(reading.getGyroscopeZ())
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
        builder.append(reading.getMagneticFieldAccuracy()).append(colSeparator)
                .append(Arrays.toString(reading.getOrientationAngleX().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getOrientationAngleY().toArray())).append(colSeparator)
                .append(Arrays.toString(reading.getOrientationAngleZ().toArray())).append(colSeparator);

        builder.append(stats.setFeatureName("ori-angle-x")
                        .calc(reading.getOrientationAngleX())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("ori-angle-y")
                        .calc(reading.getOrientationAngleY())
                        .toCsvRow(colSeparator)).append(colSeparator)
                .append(stats.setFeatureName("ori-angle-z")
                        .calc(reading.getOrientationAngleZ())
                        .toCsvRow(colSeparator)).append(colSeparator);
    }

    //#endregion
}
