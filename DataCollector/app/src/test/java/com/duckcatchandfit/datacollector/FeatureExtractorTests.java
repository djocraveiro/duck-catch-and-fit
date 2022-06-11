package com.duckcatchandfit.datacollector;

import com.duckcatchandfit.datacollector.services.FeatureExtractor;
import org.junit.Test;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

public class FeatureExtractorTests {

    //#region Setup

    private final double TOLERANCE = 0.000001;

    private Instances dataSet;
    private final FakeActivityReading activityReading = new FakeActivityReading();
    private final FeatureExtractor featureExtractor = new FeatureExtractor();

    public FeatureExtractorTests() {
        try {
            final String datasetFile = "aux-dataset.arff";
            InputStream stream = Objects.requireNonNull(this.getClass().getClassLoader()).getResourceAsStream(datasetFile);
            ConverterUtils.DataSource ds = new ConverterUtils.DataSource(stream);
            dataSet = ds.getDataSet();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //#endregion

    //#region Accelerometer

    @Test
    public void toInstance_accXMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-min").index();

        assertEquals(activityReading.accXMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mean").index();

        assertEquals(activityReading.accXMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-max").index();

        assertEquals(activityReading.accXMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-var").index();

        assertEquals(activityReading.accXVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-std").index();

        assertEquals(activityReading.accXStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accYMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-min").index();

        assertEquals(activityReading.accYMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-mean").index();

        assertEquals(activityReading.accYMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-max").index();

        assertEquals(activityReading.accYMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-var").index();

        assertEquals(activityReading.accYVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-std").index();

        assertEquals(activityReading.accYStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accZMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-min").index();

        assertEquals(activityReading.accZMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-mean").index();

        assertEquals(activityReading.accZMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-max").index();

        assertEquals(activityReading.accZMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-var").index();

        assertEquals(activityReading.accZVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-std").index();

        assertEquals(activityReading.accZStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accSignalMagnitudeArea() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-sma").index();

        assertEquals(activityReading.accSma, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accPearsonCorrelationCoefficientXY() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-pcorr-xy").index();

        assertEquals(activityReading.accPCorrXY, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accPearsonCorrelationCoefficientXZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-pcorr-xz").index();

        assertEquals(activityReading.accPCorrXZ, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accPearsonCorrelationCoefficientYZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-pcorr-yz").index();

        assertEquals(activityReading.accPCorrYZ, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXMinMaxPositionDiff() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-min-max-diff").index();

        assertEquals(activityReading.accMinMaxDiff, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXAmplitude() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-amprange").index();

        assertEquals(activityReading.accXAmplitude, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXMean1Quarter() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mean-1quarter").index();

        assertEquals(activityReading.accXMean1Quarter, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXMean2Quarter() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mean-2quarter").index();

        assertEquals(activityReading.accXMean2Quarter, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXMean3Quarter() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mean-3quarter").index();

        assertEquals(activityReading.accXMean3Quarter, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accXMean4Quarter() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mean-4quarter").index();

        assertEquals(activityReading.accXMean4Quarter, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-aptd").index();

        assertEquals(activityReading.accXAptd, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-aptd").index();

        assertEquals(activityReading.accYAptd, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-aptd").index();

        assertEquals(activityReading.accZAptd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-mad").index();

        assertEquals(activityReading.accXMad, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-mad").index();

        assertEquals(activityReading.accYMad, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-mad").index();

        assertEquals(activityReading.accZMad, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-energy").index();

        assertEquals(activityReading.accXEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-energy").index();

        assertEquals(activityReading.accYEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-energy").index();

        assertEquals(activityReading.accZEnergy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-entropy").index();

        assertEquals(activityReading.accXEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-entropy").index();

        assertEquals(activityReading.accYEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-entropy").index();

        assertEquals(activityReading.accZEntropy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_accXKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-x-kurtosis").index();

        assertEquals(activityReading.accXKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accYKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-y-kurtosis").index();

        assertEquals(activityReading.accYKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_accZKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("acc-z-kurtosis").index();

        assertEquals(activityReading.accZKurtosis, instance.value(index), TOLERANCE);
    }

    //#endregion

    //#region Gyroscope

    @Test
    public void toInstance_gyrXMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-min").index();

        assertEquals(activityReading.gyrXMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrXMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-mean").index();

        assertEquals(activityReading.gyrXMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrXMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-max").index();

        assertEquals(activityReading.gyrXMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrXVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-var").index();

        assertEquals(activityReading.gyrXVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrXStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-std").index();

        assertEquals(activityReading.gyrXStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrYMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-min").index();

        assertEquals(activityReading.gyrYMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-mean").index();

        assertEquals(activityReading.gyrYMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-max").index();

        assertEquals(activityReading.gyrYMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-var").index();

        assertEquals(activityReading.gyrYVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-std").index();

        assertEquals(activityReading.gyrYStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrZMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-min").index();

        assertEquals(activityReading.gyrZMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-mean").index();

        assertEquals(activityReading.gyrZMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-max").index();

        assertEquals(activityReading.gyrZMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-var").index();

        assertEquals(activityReading.gyrZVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-std").index();

        assertEquals(activityReading.gyrZStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrSignalMagnitudeArea() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-sma").index();

        assertEquals(activityReading.gyrSma, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrPearsonCorrelationCoefficientXY() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-pcorr-xy").index();

        assertEquals(activityReading.gyrPCorrXY, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrPearsonCorrelationCoefficientXZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-pcorr-xz").index();

        assertEquals(activityReading.gyrPCorrXZ, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrPearsonCorrelationCoefficientYZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-pcorr-yz").index();

        assertEquals(activityReading.gyrPCorrYZ, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrXAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-aptd").index();

        assertEquals(activityReading.gyrXAptd, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-aptd").index();

        assertEquals(activityReading.gyrYAptd, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZAptd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-aptd").index();

        assertEquals(activityReading.gyrZAptd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrXMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-mad").index();

        assertEquals(activityReading.gyrXMad, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-mad").index();

        assertEquals(activityReading.gyrYMad, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZMedianAbsoluteDeviation() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-mad").index();

        assertEquals(activityReading.gyrZMad, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrXEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-energy").index();

        assertEquals(activityReading.gyrXEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-energy").index();

        assertEquals(activityReading.gyrYEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-energy").index();

        assertEquals(activityReading.gyrZEnergy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrXEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-entropy").index();

        assertEquals(activityReading.gyrXEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-entropy").index();

        assertEquals(activityReading.gyrYEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-entropy").index();

        assertEquals(activityReading.gyrZEntropy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_gyrXKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-x-kurtosis").index();

        assertEquals(activityReading.gyrXKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrYKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-y-kurtosis").index();

        assertEquals(activityReading.gyrYKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_gyrZKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("gyr-z-kurtosis").index();

        assertEquals(activityReading.gyrZKurtosis, instance.value(index), TOLERANCE);
    }

    //#endregion

    //#region Magnetometer

    @Test
    public void toInstance_magXMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-x-min").index();

        assertEquals(activityReading.magXMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magXMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-x-mean").index();

        assertEquals(activityReading.magXMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magXMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-x-max").index();

        assertEquals(activityReading.magXMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magXVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-x-var").index();

        assertEquals(activityReading.magXVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magXStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-x-std").index();

        assertEquals(activityReading.magXStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magYMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-y-min").index();

        assertEquals(activityReading.magYMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-y-mean").index();

        assertEquals(activityReading.magYMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-y-max").index();

        assertEquals(activityReading.magYMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-y-var").index();

        assertEquals(activityReading.magYVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-y-std").index();

        assertEquals(activityReading.magYStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magZMin() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-z-min").index();

        assertEquals(activityReading.magZMin, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZMean() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-z-mean").index();

        assertEquals(activityReading.magZMean, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZMax() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-z-max").index();

        assertEquals(activityReading.magZMax, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZVar() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-z-var").index();

        assertEquals(activityReading.magZVar, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZStd() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("ori-angle-z-std").index();

        assertEquals(activityReading.magZStd, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magSignalMagnitudeArea() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-sma").index();

        assertEquals(activityReading.magSma, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magPearsonCorrelationCoefficientXY() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-pcorr-xy").index();

        assertEquals(activityReading.magPCorrXY, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magPearsonCorrelationCoefficientXZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-pcorr-xz").index();

        assertEquals(activityReading.magPCorrXZ, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magPearsonCorrelationCoefficientYZ() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-pcorr-yz").index();

        assertEquals(activityReading.magPCorrYZ, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magXEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-x-energy").index();

        assertEquals(activityReading.magXEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-y-energy").index();

        assertEquals(activityReading.magYEnergy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZEnergy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-z-energy").index();

        assertEquals(activityReading.magZEnergy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magXEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-x-entropy").index();

        assertEquals(activityReading.magXEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-y-entropy").index();

        assertEquals(activityReading.magYEntropy, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZEntropy() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-z-entropy").index();

        assertEquals(activityReading.magZEntropy, instance.value(index), TOLERANCE);
    }


    @Test
    public void toInstance_magXKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-x-kurtosis").index();

        assertEquals(activityReading.magXKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magYKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-y-kurtosis").index();

        assertEquals(activityReading.magYKurtosis, instance.value(index), TOLERANCE);
    }

    @Test
    public void toInstance_magZKurtosis() {
        Instance instance = featureExtractor.toInstance(activityReading);
        int index = dataSet.attribute("mag-z-kurtosis").index();

        assertEquals(activityReading.magZKurtosis, instance.value(index), TOLERANCE);
    }

    //#endregion
}