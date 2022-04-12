package com.duckcatchandfit.datacollector.utils;

import java.util.List;

public class MathHelper {

    public static float mean(List<Float> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }

        float sum = 0;
        for (float mark : values) {
            sum += mark;
        }

        return sum / (float)values.size();
    }

    public static float variance(List<Float> values)
    {
        float mean = mean(values);

        return variance(values, mean);
    }

    public static float variance(List<Float> values, float mean)
    {
        float meanSqrtDiff = 0.0f;
        int length = values.size();

        for(float num: values) {
            meanSqrtDiff += Math.pow(num - mean, 2);
        }

        return meanSqrtDiff / (float)length;
    }

    public static float standardDeviation(List<Float> values)
    {
        float variance = variance(values);

        return standardDeviation(values, variance);
    }

    public static float standardDeviation(List<Float> values, float variance)
    {
        return (float)Math.sqrt(variance);
    }
}
