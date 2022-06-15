package com.duckcatchandfit.game.services;

import com.duckcatchandfit.game.models.IActivityReading;
import com.duckcatchandfit.game.utils.MathHelper;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor
{
    //#rerion Constants

    private final int ATTRIBUTE_SIZE = 103;
    private int index = 0;
    private boolean addAccelerometerAptd = false;
    private boolean addGyroscopeAptd = false;
    private boolean addGyroscopeMad = false;

    //#endregion

    //#region Properties

    public void setAccelerometerAptd(boolean value) { addAccelerometerAptd = value; }

    public void setGyroscopeAptd(boolean value) { addGyroscopeAptd = value; }

    public void setGyroscopeMad(boolean value) { addGyroscopeMad = value; }

    //#endregion

    //#region Public Methods

    public Instance toInstance(final IActivityReading reading) {
        final Instance instance = new DenseInstance(ATTRIBUTE_SIZE);
        index = 0;

        // Index: from 0 up to 4
        addVectorStats(instance, reading.getAccelerometerX());
        // Index: from 5 up to 9
        addVectorStats(instance, reading.getAccelerometerY());
        // Index: from 10 up to 14
        addVectorStats(instance, reading.getAccelerometerZ());

        // Index: from 15 up to 19
        addVectorStats(instance, reading.getGyroscopeX());
        // Index: from 20 up to 24
        addVectorStats(instance, reading.getGyroscopeY());
        // Index: from 25 up to 29
        addVectorStats(instance, reading.getGyroscopeZ());

        // Index: from 30 up to 34
        addVectorStats(instance, reading.getOrientationAngleX());
        // Index: from 35 up to 39
        addVectorStats(instance, reading.getOrientationAngleY());
        // Index: from 40 up to 44
        addVectorStats(instance, reading.getOrientationAngleZ());

        // Accelerometer
        {
            // Index: 45
            addSignalMagnitudeArea(instance,
                    reading.getAccelerometerX(), reading.getAccelerometerY(), reading.getAccelerometerZ());

            // Index: from 46 up to 48
            addPearsonCorrelationCoefficient(instance, reading.getAccelerometerX(), reading.getAccelerometerY());
            addPearsonCorrelationCoefficient(instance, reading.getAccelerometerX(), reading.getAccelerometerZ());
            addPearsonCorrelationCoefficient(instance, reading.getAccelerometerY(), reading.getAccelerometerZ());

            // Index: 49
            addMinMaxPositionDiff(instance, reading.getAccelerometerX());

            // Index: 50
            addAmplitude(instance, reading.getAccelerometerX());

            // Index: from 51 up to 54
            addVectorQuartersMean(instance, reading.getAccelerometerX());

            // Index: from 55 up to 57
            if (addAccelerometerAptd) {
                addAveragePeakTroughDistance(instance, reading.getAccelerometerX());
                addAveragePeakTroughDistance(instance, reading.getAccelerometerY());
                addAveragePeakTroughDistance(instance, reading.getAccelerometerZ());
            }

            // Index: from 58 up to 60
            addMedianAbsoluteDeviation(instance, reading.getAccelerometerX());
            addMedianAbsoluteDeviation(instance, reading.getAccelerometerY());
            addMedianAbsoluteDeviation(instance, reading.getAccelerometerZ());

            // Index: from 61 up to 63
            addEnergy(instance, reading.getAccelerometerX());
            addEnergy(instance, reading.getAccelerometerY());
            addEnergy(instance, reading.getAccelerometerZ());

            // Index: from 64 up to 66
            addEntropy(instance, reading.getAccelerometerX());
            addEntropy(instance, reading.getAccelerometerY());
            addEntropy(instance, reading.getAccelerometerZ());

            // Index: from 67 up to 69
            addKurtosis(instance, reading.getAccelerometerX());
            addKurtosis(instance, reading.getAccelerometerY());
            addKurtosis(instance, reading.getAccelerometerZ());
        }

        // Gyroscope
        {
            // Index: 70
            addSignalMagnitudeArea(instance,
                    reading.getGyroscopeX(), reading.getGyroscopeY(), reading.getGyroscopeZ());

            // Index: from 71 up to 73
            addPearsonCorrelationCoefficient(instance, reading.getGyroscopeX(), reading.getGyroscopeY());
            addPearsonCorrelationCoefficient(instance, reading.getGyroscopeX(), reading.getGyroscopeZ());
            addPearsonCorrelationCoefficient(instance, reading.getGyroscopeY(), reading.getGyroscopeZ());

            // Index: from 74 up to 76
            if (addGyroscopeAptd) {
                addAveragePeakTroughDistance(instance, reading.getGyroscopeX());
                addAveragePeakTroughDistance(instance, reading.getGyroscopeY());
                addAveragePeakTroughDistance(instance, reading.getGyroscopeZ());
            }

            // Index: from 77 up to 79
            if (addGyroscopeMad) {
                addMedianAbsoluteDeviation(instance, reading.getGyroscopeX());
                addMedianAbsoluteDeviation(instance, reading.getGyroscopeY());
                addMedianAbsoluteDeviation(instance, reading.getGyroscopeZ());
            }

            // Index: from 80 up to 82
            addEnergy(instance, reading.getGyroscopeX());
            addEnergy(instance, reading.getGyroscopeY());
            addEnergy(instance, reading.getGyroscopeZ());

            // Index: from 83 up to 85
            addEntropy(instance, reading.getGyroscopeX());
            addEntropy(instance, reading.getGyroscopeY());
            addEntropy(instance, reading.getGyroscopeZ());

            // Index: from 86 up to 88
            addKurtosis(instance, reading.getGyroscopeX());
            addKurtosis(instance, reading.getGyroscopeY());
            addKurtosis(instance, reading.getGyroscopeZ());
        }

        // Magnetometer
        {
            // Index: 89
            addSignalMagnitudeArea(instance,
                    reading.getOrientationAngleX(), reading.getOrientationAngleY(), reading.getOrientationAngleZ());

            // Index: from 90 up to 92
            addPearsonCorrelationCoefficient(instance, reading.getOrientationAngleX(), reading.getOrientationAngleY());
            addPearsonCorrelationCoefficient(instance, reading.getOrientationAngleX(), reading.getOrientationAngleZ());
            addPearsonCorrelationCoefficient(instance, reading.getOrientationAngleY(), reading.getOrientationAngleZ());

            // Index: from 93 up to 95
            addEnergy(instance, reading.getOrientationAngleX());
            addEnergy(instance, reading.getOrientationAngleY());
            addEnergy(instance, reading.getOrientationAngleZ());

            // Index: from 96 up to 98
            addEntropy(instance, reading.getOrientationAngleX());
            addEntropy(instance, reading.getOrientationAngleY());
            addEntropy(instance, reading.getOrientationAngleZ());

            // Index: from 99 up to 101
            addKurtosis(instance, reading.getOrientationAngleX());
            addKurtosis(instance, reading.getOrientationAngleY());
            addKurtosis(instance, reading.getOrientationAngleZ());
        }

        return instance;
    }

    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attrs = new ArrayList<>(ATTRIBUTE_SIZE);

        attrs.add(new Attribute("acc-x-min"));
        attrs.add(new Attribute("acc-x-mean"));
        attrs.add(new Attribute("acc-x-max"));
        attrs.add(new Attribute("acc-x-var"));
        attrs.add(new Attribute("acc-x-std"));
        attrs.add(new Attribute("acc-y-min"));
        attrs.add(new Attribute("acc-y-mean"));
        attrs.add(new Attribute("acc-y-max"));
        attrs.add(new Attribute("acc-y-var"));
        attrs.add(new Attribute("acc-y-std"));
        attrs.add(new Attribute("acc-z-min"));
        attrs.add(new Attribute("acc-z-mean"));
        attrs.add(new Attribute("acc-z-max"));
        attrs.add(new Attribute("acc-z-var"));
        attrs.add(new Attribute("acc-z-std"));

        attrs.add(new Attribute("gyr-x-min"));
        attrs.add(new Attribute("gyr-x-mean"));
        attrs.add(new Attribute("gyr-x-max"));
        attrs.add(new Attribute("gyr-x-var"));
        attrs.add(new Attribute("gyr-x-std"));
        attrs.add(new Attribute("gyr-y-min"));
        attrs.add(new Attribute("gyr-y-mean"));
        attrs.add(new Attribute("gyr-y-max"));
        attrs.add(new Attribute("gyr-y-var"));
        attrs.add(new Attribute("gyr-y-std"));
        attrs.add(new Attribute("gyr-z-min"));
        attrs.add(new Attribute("gyr-z-mean"));
        attrs.add(new Attribute("gyr-z-max"));
        attrs.add(new Attribute("gyr-z-var"));
        attrs.add(new Attribute("gyr-z-std"));

        attrs.add(new Attribute("ori-angle-x-min"));
        attrs.add(new Attribute("ori-angle-x-mean"));
        attrs.add(new Attribute("ori-angle-x-max"));
        attrs.add(new Attribute("ori-angle-x-var"));
        attrs.add(new Attribute("ori-angle-x-std"));
        attrs.add(new Attribute("ori-angle-y-min"));
        attrs.add(new Attribute("ori-angle-y-mean"));
        attrs.add(new Attribute("ori-angle-y-max"));
        attrs.add(new Attribute("ori-angle-y-var"));
        attrs.add(new Attribute("ori-angle-y-std"));
        attrs.add(new Attribute("ori-angle-z-min"));
        attrs.add(new Attribute("ori-angle-z-mean"));
        attrs.add(new Attribute("ori-angle-z-max"));
        attrs.add(new Attribute("ori-angle-z-var"));
        attrs.add(new Attribute("ori-angle-z-std"));

        attrs.add(new Attribute("acc-sma"));
        attrs.add(new Attribute("acc-pcorr-xy"));
        attrs.add(new Attribute("acc-pcorr-xz"));
        attrs.add(new Attribute("acc-pcorr-yz"));
        attrs.add(new Attribute("acc-x-min-max-diff"));
        attrs.add(new Attribute("acc-x-amprange"));
        attrs.add(new Attribute("acc-x-mean-1quarter"));
        attrs.add(new Attribute("acc-x-mean-2quarter"));
        attrs.add(new Attribute("acc-x-mean-3quarter"));
        attrs.add(new Attribute("acc-x-mean-4quarter"));
        attrs.add(new Attribute("acc-x-aptd"));
        attrs.add(new Attribute("acc-y-aptd"));
        attrs.add(new Attribute("acc-z-aptd"));
        attrs.add(new Attribute("acc-x-mad"));
        attrs.add(new Attribute("acc-y-mad"));
        attrs.add(new Attribute("acc-z-mad"));
        attrs.add(new Attribute("acc-x-energy"));
        attrs.add(new Attribute("acc-y-energy"));
        attrs.add(new Attribute("acc-z-energy"));
        attrs.add(new Attribute("acc-x-entropy"));
        attrs.add(new Attribute("acc-y-entropy"));
        attrs.add(new Attribute("acc-z-entropy"));
        attrs.add(new Attribute("acc-x-kurtosis"));
        attrs.add(new Attribute("acc-y-kurtosis"));
        attrs.add(new Attribute("acc-z-kurtosis"));
        attrs.add(new Attribute("gyr-sma"));
        attrs.add(new Attribute("gyr-pcorr-xy"));
        attrs.add(new Attribute("gyr-pcorr-xz"));
        attrs.add(new Attribute("gyr-pcorr-yz"));
        attrs.add(new Attribute("gyr-x-aptd"));
        attrs.add(new Attribute("gyr-y-aptd"));
        attrs.add(new Attribute("gyr-z-aptd"));
        attrs.add(new Attribute("gyr-x-mad"));
        attrs.add(new Attribute("gyr-y-mad"));
        attrs.add(new Attribute("gyr-z-mad"));
        attrs.add(new Attribute("gyr-x-energy"));
        attrs.add(new Attribute("gyr-y-energy"));
        attrs.add(new Attribute("gyr-z-energy"));
        attrs.add(new Attribute("gyr-x-entropy"));
        attrs.add(new Attribute("gyr-y-entropy"));
        attrs.add(new Attribute("gyr-z-entropy"));
        attrs.add(new Attribute("gyr-x-kurtosis"));
        attrs.add(new Attribute("gyr-y-kurtosis"));
        attrs.add(new Attribute("gyr-z-kurtosis"));
        attrs.add(new Attribute("mag-sma"));
        attrs.add(new Attribute("mag-pcorr-xy"));
        attrs.add(new Attribute("mag-pcorr-xz"));
        attrs.add(new Attribute("mag-pcorr-yz"));
        attrs.add(new Attribute("mag-x-energy"));
        attrs.add(new Attribute("mag-y-energy"));
        attrs.add(new Attribute("mag-z-energy"));
        attrs.add(new Attribute("mag-x-entropy"));
        attrs.add(new Attribute("mag-y-entropy"));
        attrs.add(new Attribute("mag-z-entropy"));
        attrs.add(new Attribute("mag-x-kurtosis"));
        attrs.add(new Attribute("mag-y-kurtosis"));
        attrs.add(new Attribute("mag-z-kurtosis"));

        attrs.add(new Attribute("activity-class"));

        return attrs;
    }

    //#enregion

    //#region Private Methods

    private void addVectorStats(Instance instance, List<Float> values) {
        final float mean = MathHelper.mean(values);
        final float variance = MathHelper.variance(values, mean);

        instance.setValue(index++, MathHelper.min(values));
        instance.setValue(index++, mean);
        instance.setValue(index++, MathHelper.max(values));
        instance.setValue(index++, variance);
        instance.setValue(index++, MathHelper.stdDeviation(values, variance));
    }

    private void addSignalMagnitudeArea(Instance instance, List<Float> xValues, List<Float> yValues,
            List<Float> zValues) {
        final int size = xValues.size();
        float totalSum = 0;

        totalSum += MathHelper.sum(MathHelper.abs(xValues));
        totalSum += MathHelper.sum(MathHelper.abs(yValues));
        totalSum += MathHelper.sum(MathHelper.abs(zValues));

        final float sma = totalSum / size;
        instance.setValue(index++, sma);
    }

    private void addPearsonCorrelationCoefficient(Instance instance, List<Float> aValues, List<Float> bValues) {
        float correlation = 0;
        float covariance = 0;

        if (aValues.size() > 0 && bValues.size() > 0)
        {
            float meanA = MathHelper.mean(aValues);
            float meanB = MathHelper.mean(bValues);
            float stdDeviationA = MathHelper.stdDeviation(aValues);
            float stdDeviationB = MathHelper.stdDeviation(bValues);

            for (int i = 0; i < aValues.size(); i++)
            {
                covariance += (aValues.get(i) - meanA) * (bValues.get(i) - meanB);
            }

            covariance = covariance / (float) aValues.size();

            if (stdDeviationA * stdDeviationB != 0)
            {
                correlation = covariance / (stdDeviationA * stdDeviationB);
            }
        }

        instance.setValue(index++, correlation);
    }

    private void addMinMaxPositionDiff(Instance instance, List<Float> values) {
        int maxIndex = MathHelper.maxIndex(values);
        int minIndex = MathHelper.minIndex(values);
        instance.setValue(index++, maxIndex - minIndex);
    }

    private void addAmplitude(Instance instance, List<Float> values) {
        instance.setValue(index++, MathHelper.max(values) - MathHelper.min(values));
    }

    private void addVectorQuartersMean(Instance instance, List<Float> values) {
        int quarterSize = values.size() / 4;
        int curQuarterStart = 0;

        for (int i = 1; i <= 4; i++) {
            List<Float> quarterValues = values.subList(curQuarterStart, curQuarterStart + quarterSize);
            instance.setValue(index++, MathHelper.mean(quarterValues));

            curQuarterStart += quarterSize;
        }
    }

    private void addAveragePeakTroughDistance(Instance instance, List<Float> values) {
        float peakValue = values.get(0);
        float troughValue = values.get(0);
        float prevSlope = 0;
        float curSlope = 0, curValue = 0, prevValue = 0;
        float sum = 0;
        int arraySize = values.size();
        int evaluateSize = arraySize + 1;

        for (int i = 0; i < evaluateSize; i++) {
            if (i == 0) {
                curValue = values.get(i);
                continue;
            }

            prevValue = curValue;
            prevSlope = curSlope;

            if (i < arraySize) {
                curValue = values.get(i);
                curSlope = curValue - prevValue;
            }
            else {
                curSlope = prevSlope * -1;
            }

            //peak found
            if (prevSlope > 0 && curSlope <= 0) {
                peakValue = prevValue;
                sum += peakValue - troughValue;
            }
            //trough found
            else if (prevSlope < 0 && curSlope >= 0) {
                troughValue = prevValue;
                sum += troughValue - peakValue;
            }
        }

        instance.setValue(index++, sum / arraySize);
    }

    private void addMedianAbsoluteDeviation(Instance instance, List<Float> values) {
        instance.setValue(index++, MathHelper.medianAbsoluteDeviation(values));
    }

    private void addEnergy(Instance instance, List<Float> values) {
        float[] fftValues = MathHelper.calFft(values);
        float energy = 0;

        for (Float fftValue : fftValues) {
            energy += Math.pow(fftValue, 2);
        }

        if (fftValues.length > 0)
        {
            energy = energy / (float)fftValues.length;
        }

        instance.setValue(index++, energy);
    }

    private void addEntropy(Instance instance, List<Float> values) {
        float[] fftValues = MathHelper.calFft(values);
        float entropy = 0;
        float[] psd = new float[fftValues.length];

        if (fftValues.length > 0)
        {
            // Calculate Power Spectral Density
            for (int i = 0; i < fftValues.length; i++)
            {
                psd[i] = (float) (Math.pow(fftValues[i], 2) / (float)fftValues.length);
            }

            float psdSum = MathHelper.sum(psd);

            if (psdSum > 0)
            {
                // Normalize calculated PSD so that it can be viewed as a Probability Density Function
                for (int i = 0; i < fftValues.length; i++)
                {
                    psd[i] = psd[i] / psdSum;
                }

                // Calculate the Frequency Domain Entropy
                for (int i = 0; i < fftValues.length; i++)
                {
                    if (psd[i] != 0)
                    {
                        entropy += psd[i] * Math.log(psd[i]);
                    }
                }

                entropy *= -1;
            }
        }

        instance.setValue(index++, entropy);
    }

    private void addKurtosis(Instance instance, List<Float> values) {
        instance.setValue(index++, MathHelper.kurtosis(values));
    }

    //#endregion
}
