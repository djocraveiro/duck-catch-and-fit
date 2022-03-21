package com.duckcatchandfit.datacollector;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HumanActivityReading implements Cloneable, ICsvData {

    //#region Constants

    public static final String JUMP_LEFT = "jump_left";
    public static final String JUMP_RIGHT = "jump_right";

    //#endregion

    //#region Fields

    private final int instanceSize;
    private Date startDate;
    private Date endDate;
    private List<float[]> accelerometerReadings;
    private List<float[]> gyroscopeReadings;
    private String label;

    //#endregion

    //#region Initializers

    public HumanActivityReading(int instanceSize) {
        this.instanceSize = instanceSize;
        this.startDate = new Date();
        this.endDate = new Date();
        this.accelerometerReadings = new ArrayList<>(instanceSize);
        this.gyroscopeReadings = new ArrayList<>(instanceSize);
        this.label = "";
    }

    //#endregion

    //#region Properties

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public List<float[]> getAccelerometerReadings() { return accelerometerReadings; }

    public List<float[]> getGyroscopeReadings() { return gyroscopeReadings; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    //#endregion

    //#region Public Methods

    //#region Cloneable Methods

    @NonNull
    @Override
    public Object clone() {
        HumanActivityReading clone = new HumanActivityReading(this.instanceSize);

        clone.startDate = (Date)this.startDate.clone();
        clone.endDate = (Date)this.endDate.clone();
        clone.accelerometerReadings = this.accelerometerReadings.stream()
            .map(float[]::clone).collect(toList());
        clone.gyroscopeReadings = this.gyroscopeReadings.stream()
                .map(float[]::clone).collect(toList());

        return clone;
    }

    //#endregion

    //#region ICsvData Methods

    @Override
    public String toCsvHeader(String colSeparator) {
        return "startDate" + colSeparator +
            "endDate" + colSeparator +
            "accelerometer" + colSeparator +
            "gyroscope" + colSeparator +
            "label";
    }

    @Override
    public String toCsvRow(String colSeparator) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat.format(getStartDate()) + colSeparator +
                dateFormat.format(getEndDate()) + colSeparator +
            getAccelerometerReadings().stream()
                .map(n -> Arrays.toString(n))
                .collect(Collectors.joining(",", "[", "]")) + colSeparator +
            getGyroscopeReadings().stream()
                    .map(n -> Arrays.toString(n))
                    .collect(Collectors.joining(",", "[", "]")) + colSeparator +
            getLabel();
    }

    //#endregion

    //#endregion
}
