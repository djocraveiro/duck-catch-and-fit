package com.duckcatchandfit.datacollector.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

// Formulas src: https://github.com/hcmlab/ssj/blob/master/libssj/src/main/java/hcm/ssj/body/AccelerationFeatures.java

public class MathHelper {

    public static float sum(final List<Float> values) {
        float sum = 0;

        for (float value : values) {
            sum += value;
        }

        return sum;
    }

    public static float sum(float[] values) {
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

    public static int minIndex(final List<Float> values) {
        if (values.isEmpty()) {
            return 0;
        }

        float min = Float.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < values.size(); i++) {
            Float value = values.get(i);

            if (value < min) {
                min = value;
                minIndex = i;
            }
        }

        return minIndex;
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

    public static float median(final List<Float> values) {
        float median = 0;
        int size = values.size();

        if (size > 0) {
            // Copy values for sorting
            List<Float> valueCopies = new ArrayList(values);

            // Sort values ascending
            Collections.sort(valueCopies);

            if (size % 2 == 0) {
                // Even
                median = (valueCopies.get(size / 2 - 1) + valueCopies.get(size / 2)) / 2.0f;
            }
            else {
                // Odd
                median = valueCopies.get((size - 1) / 2);
            }
        }

        return median;
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

    public static int maxIndex(final List<Float> values) {
        if (values.isEmpty()) {
            return 0;
        }

        float max = -Float.MAX_VALUE;
        int maxIndex = 0;

        for (int i = 0; i < values.size(); i++) {
            Float value = values.get(i);

            if (value > max) {
                max = value;
                maxIndex = i;
            }
        }

        return maxIndex;
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

    public static float meanAbsoluteDeviation(final List<Float> values) {
        float mad = 0;
        float mean = MathHelper.mean(values);

        if (values.size() > 0) {
            for (Float value : values) {
                mad += Math.abs(value - mean);
            }

            mad = (float) Math.sqrt(mad / (float) values.size());
        }

        return mad;
    }

    public static float medianAbsoluteDeviation(final List<Float> values) {
        float mad = 0;

        if (values.isEmpty()) {
            return mad;
        }

        float median = MathHelper.median(values);
        List<Float> medianDiff = new ArrayList<>(values.size());

        for (Float value : values) {
            medianDiff.add(Math.abs(value - median));
        }

        mad = MathHelper.median(medianDiff);

        return mad;
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

    public static List<Float> abs(List<Float> values) {
        if (values.isEmpty()) {
            return values;
        }

        List<Float> result = new ArrayList<>(values.size());

        for (Float value : values) {
            result.add(Math.abs(value));
        }

        return result;
    }

    private static final FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
    public static float[] calFft(List<Float> values) {
        final Complex[] input = new Complex[values.size()];

        for (int i = 0; i < input.length; i++) {
            input[i] = new Complex(values.get(i));
        }

        Complex[] result = fft.transform(input, TransformType.FORWARD);
        float[] magFft = new float[result.length];

        for (int i = 0; i < input.length; i++) {
            magFft[i] = (float)Math.sqrt(
                Math.pow(result[i].getReal(), 2) + Math.pow(result[i].getImaginary(), 2));
        }

        return magFft;
    }
}
