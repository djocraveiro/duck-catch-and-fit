package com.duckcatchandfit.datacollector.services;

import com.duckcatchandfit.datacollector.models.ActivityReading;
import com.duckcatchandfit.datacollector.utils.MathHelper;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.util.List;

public class FeatureExtractor
{
    //#region Public Methods

    public static Instance toInstance(final ActivityReading reading) {
        final Instance instance = new DenseInstance(45);
        int index = 0;

        // Index: from 0 up to 4
        index = enrichWithStats(instance, index, reading.getAccelerometerX());
        // Index: from 5 up to 9
        index = enrichWithStats(instance, index, reading.getAccelerometerY());
        // Index: from 9 up to 13
        index = enrichWithStats(instance, index, reading.getAccelerometerZ());

        // Index: from 14 up to 18
        index = enrichWithStats(instance, index, reading.getGyroscopeX());
        // Index: from 19 up to 23
        index = enrichWithStats(instance, index, reading.getGyroscopeY());
        // Index: from 24 up to 28
        index = enrichWithStats(instance, index, reading.getGyroscopeZ());

        // Index: from 29 up to 33
        index = enrichWithStats(instance, index, reading.getGyroscopeX());
        // Index: from 34 up to 38
        index = enrichWithStats(instance, index, reading.getGyroscopeY());
        // Index: from 39 up to 43
        index = enrichWithStats(instance, index, reading.getGyroscopeZ());

        return instance;
    }

    //#enregion

    //#region Private Methods

    private static int enrichWithStats(Instance instance, int index, List<Float> values) {
        final float mean = MathHelper.mean(values);
        final float variance = MathHelper.variance(values, mean);

        instance.setValue(index++, MathHelper.min(values));
        instance.setValue(index++, mean);
        instance.setValue(index++, MathHelper.max(values));
        instance.setValue(index++, variance);
        instance.setValue(index++, MathHelper.stdDeviation(values, variance));

        return index;
    }

    //#endregion
}
