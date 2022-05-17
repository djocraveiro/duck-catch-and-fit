package com.duckcatchandfit.datacollector.utils;

import java.util.List;

public class MathHelper {

    public static float sum(final List<Float> values) {
        float sum = 0;

        for (float value : values) {
            sum += value;
        }

        return sum;
    }

    public static float min(final List<Float> values) {
        if (values.isEmpty()) {
            return 0;
        }

        float min = Float.MAX_VALUE;

        for (Float value : values) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }

    public static float mean(final List<Float> values) {
        return mean(values, sum(values));
    }

    public static float mean(final List<Float> values, float sum) {
        if (values.isEmpty()) {
            return 0;
        }

        return sum / (float)values.size();
    }

    public static float max(final List<Float> values) {
        if (values.isEmpty()) {
            return 0;
        }

        float max = -Float.MAX_VALUE;

        for (Float value : values) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    public static float variance(final List<Float> values) {
        return variance(values, mean(values));
    }

    public static float variance(final List<Float> values, final float mean) {
        if (values.isEmpty()) {
            return 0;
        }

        float meanSqrtDiff = 0;

        for(float num: values) {
            meanSqrtDiff += Math.pow(num - mean, 2);
        }

        return meanSqrtDiff / (float)values.size();
    }

    public static float stdDeviation(final List<Float> values) {
        return stdDeviation(values, variance(values));
    }

    public static float stdDeviation(final List<Float> values, final float variance) {
        if (values.isEmpty()) {
            return 0;
        }

        return (float)Math.sqrt(variance);
    }

    public static float kurtosis(final List<Float> values) {
        return kurtosis(values, mean(values), stdDeviation(values));
    }

    public static float kurtosis(final List<Float> values, final float mean, final float stdDeviation) {
        if (values.isEmpty() || stdDeviation <= 0) {
            return 0;
        }

        float kurtosis = 0;

        for (Float value : values) {
            kurtosis += Math.pow((value - mean) / stdDeviation, 4);
        }

        kurtosis = kurtosis / (float)values.size();

        return kurtosis;
    }

    public static float range(List<Float> values) {
        return max(values) - min(values);
    }

    public static float range(float max, float min) {
        return max - min;
    }

}
