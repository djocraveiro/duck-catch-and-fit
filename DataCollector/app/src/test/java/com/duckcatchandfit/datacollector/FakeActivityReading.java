package com.duckcatchandfit.datacollector;

import com.duckcatchandfit.datacollector.models.IActivityReading;
import com.duckcatchandfit.datacollector.utils.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FakeActivityReading implements IActivityReading {

    //#region Public Methods

    //#region IActivityReading

    @Override
    public boolean isFullFilled() {
        return true;
    }

    @Override
    public Date getStartDate() {
        return dateFromString("2022-04-20T23:30:22.835Z");
    }

    @Override
    public Date getEndDate() {
        return dateFromString("2022-04-20T23:30:24.111Z");
    }

    @Override
    public int getAccelerometerAccuracy() {
        return 2;
    }

    @Override
    public List<Float> getAccelerometerX() {
        Float[] array = new Float[] { 0.04962164f, 0.04962164f, 0.04962164f, 0.04962164f, 0.04962164f, 0.04962164f, 0.04962164f, -0.66660583f, -0.66660583f, -1.231f, -1.231f, -1.231f, -1.231f, -1.231f, -1.231f, -0.3093114f, -0.3093114f, -0.3093114f, -0.3093114f, -0.3093114f, 0.25718713f, 0.8262453f, 0.8262453f, 0.8262453f, 0.8262453f, 0.8262453f, -0.73108625f, -0.73108625f, 0.06886268f, 0.06886268f, 0.06886268f, 0.72461414f, 0.72461414f, 0.72461414f, 1.3221442f, 1.3221442f, 0.5049319f, 0.5049319f, 0.5049319f, 0.5049319f, 0.5049319f, -0.26393008f, -0.87225246f, -1.6742179f, 0.2634883f, -0.9819827f, -0.9819827f, -3.0970974f, -3.0970974f, -0.22487688f, 1.988946f, 2.6266427f, 1.4049553f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.75398123f, 0.0809626f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getAccelerometerY() {
        Float[] array = new Float[] { 0.2781539f, -0.2528081f, -0.2528081f, -0.2528081f, -0.2528081f, -0.2528081f, -0.2528081f, 0.40195274f, 1.4614296f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 2.0566926f, 1.4150143f, 0.60347176f, -0.8955555f, -2.7732687f, -2.7732687f, -3.440549f, -3.440549f, -3.440549f, -3.440549f, -2.8720493f, -2.00422f, -2.00422f, -1.3605893f, -1.3605893f, -1.3605893f, -1.3605893f, -1.3605893f, -0.7273904f, -0.7273904f, -0.009130955f, -0.009130955f, 0.6236279f, 1.7572641f, 2.9099374f, 5.4520698f, 5.4520698f, 6.568778f, 6.568778f, 1.8858328f, -0.11223221f, -0.6598177f, -0.6598177f, -1.5867987f, -2.3178153f, -2.9660563f, -2.9660563f, -2.9660563f, -1.9673047f, -1.1078038f, -0.43418026f, 0.7398224f, 0.7398224f, 0.7398224f, 0.7398224f, 0.7398224f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getAccelerometerZ() {
        Float[] array = new Float[] { -0.35332966f, -0.9656162f, -0.9656162f, -0.9656162f, -0.9656162f, -0.18897343f, -0.18897343f, -0.18897343f, 0.5378156f, 1.1849537f, 1.1849537f, 2.214552f, 3.2642326f, 3.2642326f, 2.75072f, 2.75072f, 1.9246645f, 1.9246645f, 1.9246645f, 2.5298872f, 1.3824787f, -0.6836777f, -4.1592836f, -4.1592836f, -3.0008163f, -3.0008163f, -1.9324284f, -3.048811f, -4.3598127f, -4.327312f, -2.6675258f, -1.0880122f, -1.0880122f, -1.0880122f, -1.0880122f, -1.8586545f, -0.86937284f, 0.15610075f, 0.15610075f, 0.15610075f, 0.15610075f, 0.15610075f, 1.3171039f, 2.7003627f, 2.006199f, 5.6760406f, 5.6760406f, 11.512476f, 11.512476f, 5.683256f, -0.46128082f, -1.9898682f, -1.9898682f, -2.7105083f, -2.7105083f, -3.555419f, -3.555419f, -3.555419f, -2.1182833f, -1.2675514f, -0.3737774f, -0.3737774f, 0.36848307f, 0.8914294f };
        return listFromArray(array);
    }

    @Override
    public int getGyroscopeAccuracy() {
        return 3;
    }

    @Override
    public List<Float> getGyroscopeX() {
        Float[] array = new Float[] { 0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.11521237f, 0.0f, 0.0f, 0.0f, -0.0f, -0.0f, -0.025789252f, 0.0f, -0.0f, -0.0f, -0.0f, -0.0f, 0.0f, 0.05005602f, 0.0f, -0.0f, -0.0f, -0.0f, 0.0f, 0.0f, 0.07429222f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.054563724f, 0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.05496142f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1560755f, 0.0f, 0.0f, 0.0f, 0.0f, -0.0016498059f, -0.0016498059f, -0.0f, -0.0f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getGyroscopeY() {
        Float[] array = new Float[] { -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.07135294f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.095743895f, -0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.011582122f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.011555565f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0043480783f, 0.0f, 0.0f, 0.0f, -0.0f, -0.0f, -0.0f, -0.0575108f, -0.0f, -0.0f, -0.0f, 0.0f, -0.0f, -0.0f, -0.078403726f, -0.0f, -0.0f, -0.0f, -0.0f, -0.015537463f, -0.015537463f, -0.0f, -0.0f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getGyroscopeZ() {
        Float[] array = new Float[] { -0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.063393764f, 0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.06564463f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.05089546f, -0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.03177244f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.036394387f, 0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.0f, -0.04455274f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.06344472f, 0.0f, 0.0f, 0.0f, -0.0f, -0.0033950717f, -0.0033950717f, -0.0f, -0.0f };
        return listFromArray(array);
    }

    @Override
    public int getMagneticFieldAccuracy() {
        return 1;
    }

    @Override
    public List<Float> getOrientationAngleX() {
        Float[] array = new Float[] { -2.7557578f, -2.7604296f, -2.7584076f, -2.7584076f, -2.756165f, -2.755597f, -2.755206f, -2.753397f, -2.7481716f, -2.743113f, -2.743113f, -2.741799f, -2.7466407f, -2.753643f, -2.762622f, -2.7639377f, -2.7727883f, -2.777378f, -2.777378f, -2.781269f, -2.8039482f, -2.8340218f, -2.8191946f, -2.8119812f, -2.800862f, -2.800862f, -2.7598898f, -2.665706f, -2.553973f, -2.5219934f, -2.523422f, -2.5335388f, -2.5335388f, -2.540452f, -2.545072f, -2.5414402f, -2.5426462f, -2.5679057f, -2.587088f, -2.587088f, -2.597067f, -2.580921f, -2.5717237f, -2.530781f, -2.5294323f, -2.542733f, -2.542733f, -2.5849566f, -2.5986881f, -2.5803657f, -2.5681238f, -2.545002f, -2.552051f, -2.5588663f, -2.5588663f, -2.564863f, -2.5688229f, -2.5673165f, -2.5758162f, -2.631939f, -2.6412752f, -2.6412752f, -2.6501813f, -2.6679816f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getOrientationAngleY() {
        Float[] array = new Float[] { -0.48419833f, -0.49290532f, -0.50461614f, -0.50461614f, -0.5126826f, -0.51441276f, -0.5161485f, -0.5208823f, -0.5548778f, -0.58113587f, -0.58113587f, -0.5936454f, -0.58694464f, -0.5862968f, -0.59372675f, -0.60349274f, -0.6215069f, -0.62238467f, -0.62238467f, -0.61772954f, -0.6142525f, -0.61087495f, -0.60944533f, -0.6148948f, -0.59285235f, -0.59285235f, -0.5505431f, -0.52018374f, -0.50824285f, -0.5145835f, -0.5103166f, -0.4856804f, -0.4856804f, -0.45818353f, -0.44258207f, -0.43119493f, -0.4240239f, -0.3749746f, -0.37479886f, -0.37479886f, -0.41886598f, -0.5185215f, -0.6237292f, -0.7791524f, -0.86365855f, -0.8625144f, -0.8625144f, -0.71038944f, -0.5778023f, -0.52356184f, -0.52529347f, -0.5395591f, -0.5204472f, -0.4977105f, -0.4977105f, -0.47970092f, -0.4795443f, -0.4923014f, -0.5095222f, -0.5594537f, -0.59087557f, -0.59087557f, -0.6087519f, -0.6123465f };
        return listFromArray(array);
    }

    @Override
    public List<Float> getOrientationAngleZ() {
        Float[] array = new Float[] { 0.09216724f, 0.11223705f, 0.124161996f, 0.124161996f, 0.12282244f, 0.12154139f, 0.13538435f, 0.15693146f, 0.20275414f, 0.23319182f, 0.23319182f, 0.2575307f, 0.27651584f, 0.2932204f, 0.29664594f, 0.28769058f, 0.2859794f, 0.28745022f, 0.28745022f, 0.27938792f, 0.26818442f, 0.25703725f, 0.25159347f, 0.24366081f, 0.25231764f, 0.25231764f, 0.2829346f, 0.31927928f, 0.36003605f, 0.49534708f, 0.5383871f, 0.53513163f, 0.53513163f, 0.5087504f, 0.47798702f, 0.45074064f, 0.4397368f, 0.39981645f, 0.35342336f, 0.35342336f, 0.32956368f, 0.3374613f, 0.36581963f, 0.4123494f, 0.3637488f, 0.32322705f, 0.32322705f, 0.30481324f, 0.30168095f, 0.27578625f, 0.2438315f, 0.17134716f, 0.14841788f, 0.14089079f, 0.14089079f, 0.14628756f, 0.15127754f, 0.14764547f, 0.13754766f, 0.12393305f, 0.11346664f, 0.11346664f, 0.100298904f, 0.09494035f };
        return listFromArray(array);
    }

    @Override
    public String getActivity() {
        return "jump_left";
    }

    @Override
    public String getDeviceId() {
        return "fake-device-id-a5ad";
    }

    @Override
    public int getInstanceSize() {
        return 64;
    }

    //#endregion

    //#region Accelerometer

    public final float accXMin = -3.0970974f;
    public final float accXMean = 0.051071487f;
    public final float accXMax = 2.6266427000000006f;
    public final float accXVar = 1.0094533f;
    public final float accXStd = 1.0047156f;

    public final float accYMin = -3.440549f;
    public final float accYMean = 0.07848219599999999f;
    public final float accYMax = 6.568778f;
    public final float accYVar = 5.3374724f;
    public final float accYStd = 2.3102970000000003f;

    public final float accZMin = -4.3598127f;
    public final float accZMean = 0.11091678f;
    public final float accZMax = 11.512476f;
    public final float accZVar = 9.843419f;
    public final float accZStd = 3.1374223f;

    public final float accSma = 4.7844445618750004f;
    public final float accPCorrXY = -0.5233921900079217f;
    public final float accPCorrXZ = -0.7416859885092927f;
    public final float accPCorrYZ = 0.6804506145293179f;
    public final float accMinMaxDiff = 4.0f;
    public final float accXAmplitude = 5.7237401000000006f;
    public final float accXMean1Quarter = -0.54257322375f;
    public final float accXMean2Quarter = 0.16376235687499996f;
    public final float accXMean3Quarter = -0.061862422499999986f;
    public final float accXMean4Quarter = 0.6449590387499999f;
    public final float accXAptd = -0.022545208749999997f;
    public final float accYAptd = 0.108696806953125f;
    public final float accZAptd = -0.2194088065625f;
    public final float accXMad = 0.68511855f;
    public final float accYMad = 1.69103005f;
    public final float accZMad = 1.6534860500000002f;
    public final float accXEnergy = 64.77193600928157f;
    public final float accYEnergy = 341.99238614013325f;
    public final float accZEnergy = 630.7662579050136f;
    public final float accXEntropy = 3.0783016668662047f;
    public final float accYEntropy = 2.2803544906710327f;
    public final float accZEntropy = 2.518117847455322f;
    public final float accXKurtosis = 4.5644887300052766f;
    public final float accYKurtosis = 3.653538991165044f;
    public final float accZKurtosis = 6.403671140932555f;

    //#endregion

    //#region Gyroscope

    public final float gyrXMin = -0.05496142f;
    public final float gyrXMean = 0.0057210876f;
    public final float gyrXMax = 0.1560755f;
    public final float gyrXVar = 0.0007848764f;
    public final float gyrXStd = 0.028015645f;

    public final float gyrYMin = -0.07840372599999999f;
    public final float gyrYMean = 7.003138000000001e-05f;
    public final float gyrYMax = 0.095743895f;
    public final float gyrYVar = 0.00038252919999999996f;
    public final float gyrYStd = 0.019558353f;

    public final float gyrZMin = -0.06564463f;
    public final float gyrZMean = 0.00042378652000000003f;
    public final float gyrZMax = 0.06344472f;
    public final float gyrZVar = 0.00030115803f;
    public final float gyrZStd = 0.017353905f;

    public final float gyrSma = 0.0196673508515625f;
    public final float gyrPCorrXY = -0.09525189250610219f;
    public final float gyrPCorrXZ = 0.7392498750089606f;
    public final float gyrPCorrYZ = -0.2241253063104856f;
    public final float gyrXAptd = -0.00382970190625f;
    public final float gyrYAptd = -6.835367656250022e-05f;
    public final float gyrZAptd = -0.0010591354843750003f;
    public final float gyrXMad = 0.0f;
    public final float gyrYMad = 0.0f;
    public final float gyrZMad = 0.0f;
    public final float gyrXEnergy = 0.05232687789493513f;
    public final float gyrYEnergy = 0.024482179774085473f;
    public final float gyrZEnergy = 0.019285606120277854f;
    public final float gyrXEntropy = 3.804297530022186f;
    public final float gyrYEntropy = 3.7358906831873595f;
    public final float gyrZEntropy = 3.8229005047455824f;
    public final float gyrXKurtosis = 17.781305136094417f;
    public final float gyrYKurtosis = 16.943439694512684f;
    public final float gyrZKurtosis = 11.063557103014135f;

    //#endregion

    //#region Magnetometer

    public final float magXMin = -2.8340218f;
    public final float magXMean = -2.65612f;
    public final float magXMax = -2.5219934f;
    public final float magXVar = 0.010666696000000002f;
    public final float magXStd = 0.1032797f;

    public final float magYMin = -0.86365855f;
    public final float magYMean = -0.5554919f;
    public final float magYMax = -0.37479886f;
    public final float magYVar = 0.010166378f;
    public final float magYStd = 0.100828454f;

    public final float magZMin = 0.09216724f;
    public final float magZMean = 0.2671496f;
    public final float magZMax = 0.5383871f;
    public final float magZVar = 0.0149376625f;
    public final float magZStd = 0.12221973400000001f;

    public final float magSma = 3.478760708375f;
    public final float magPCorrXY = 0.13280839282386117f;
    public final float magPCorrXZ = 0.4273373108126142f;
    public final float magPCorrYZ = 0.04110647700908269f;
    public final float magXEnergy = 452.20068413551513f;
    public final float magYEnergy = 20.399213316885234f;
    public final float magZEnergy = 5.523619951010628f;
    public final float magXEntropy = 0.013791474749598683f;
    public final float magYEntropy = 0.22336356136538696f;
    public final float magZEntropy = 0.7386277068463062f;
    public final float magXKurtosis = 1.3491770811081674f;
    public final float magYKurtosis = 5.233109629130192f;
    public final float magZKurtosis = 2.4931210103974126f;

    //#endregion

    //#endregion

    //#region Private Methods

    private Date dateFromString(String value) {
        SimpleDateFormat formatter = DateTime.getISO8601DateFormat();

        try {
            return formatter.parse(value);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> List<T> listFromArray(T[] array) {
        List<T> list = new ArrayList<>(array.length);
        Collections.addAll(list, array);

        return list;
    }

    //#endregion
}
