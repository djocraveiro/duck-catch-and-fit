package com.duckcatchandfit.datacollector.storage;

import com.duckcatchandfit.datacollector.utils.MathHelper;

import java.util.List;

public class FeatureStats implements ICsvData {

    //#region Fields

    private String featureName = "";
    private float min = 0.0f;
    private float mean = 0.0f;
    private float max = 0.0f;
    private float variance = 0.0f;
    private float std = 0.0f;

    //#endregion

    //#region Properties

    public FeatureStats setFeatureName(String featureName) {
        this.featureName = featureName;

        return this;
    }

    //#endregion

    //#region Public Methods

    public FeatureStats calc(List<Float> values) {
        min = MathHelper.min(values);
        mean = MathHelper.mean(values);
        max = MathHelper.max(values);
        variance = MathHelper.variance(values, mean);
        std = MathHelper.stdDeviation(values, variance);

        return this;
    }

    @Override
    public String toCsvHeader(String colSeparator) {
        return featureName + "-min" + colSeparator +
            featureName + "-mean" + colSeparator +
            featureName + "-max" + colSeparator +
            featureName + "-var" + colSeparator +
            featureName + "-std";
    }

    @Override
    public String toCsvRow(String colSeparator) {
        return min + colSeparator +
            mean + colSeparator +
            max + colSeparator +
            variance + colSeparator +
            std;
    }

    //#endregion
}
