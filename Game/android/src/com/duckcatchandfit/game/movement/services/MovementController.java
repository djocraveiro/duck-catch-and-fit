package com.duckcatchandfit.game.movement.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.duckcatchandfit.game.IGameControls;
import com.duckcatchandfit.game.movement.models.ActivityReading;
import com.duckcatchandfit.game.utils.ApplicationExecutors;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class MovementController {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private Context context;

    private final DataCollector dataCollector = new DataCollector();
    private final FeatureExtractor featureExtractor = new FeatureExtractor();
    private final Classifier classifier;
    private final Instances dataSet;
    private long lastLateralPrediction = 0;
    private String lastActivity = "";
    private IGameControls gameControls = null;

    private final ICollectListener collectListener = new ICollectListener() {
        @Override
        public void onCollectStop() {

        }

        @Override
        public void onInstanceCollected(final ActivityReading reading) {
            final String activityLabel = predictClass(reading);
            if (activityLabel.isEmpty() || lastActivity.equals(activityLabel)) {
                return;
            }

            lastActivity = activityLabel;

            exec.getMainThread().execute(() -> {
                if (gameControls != null) {
                    //showToast(activityLabel);

                    switch (activityLabel) {
                        case ActivityReading.JUMP_LEFT:
                            gameControls.getPlayerControls().movePlayerLeft();
                            break;

                        case ActivityReading.JUMP_RIGHT:
                            gameControls.getPlayerControls().movePlayerRight();
                            break;
                    }
                }
            });
        }
    };

    //#endregion

    //#region Initializers

    public MovementController(Context context) {
        this.context = context;
        this.classifier = loadModel();
        this.dataSet = loadDataSet();

        dataCollector.setCollectListener(collectListener);
    }

    //#endregion

    //#region Public Methods

    public void startListening(final IGameControls gameControls) {
        this.gameControls = gameControls;

        dataCollector.startListeningSensors(context);
        dataCollector.startReadingInstance(DataCollector.COLLECT_MODE_INSTANCE_LOOP);
    }

    public void stopListening(final IGameControls gameControls) {
        stopListeningSensors();

        this.gameControls = null;
    }

    public void dispose() {
        stopListeningSensors();

        context = null;
        gameControls = null;
    }

    //#endregion

    //#region Private Methods

    private void stopListeningSensors() {
        dataCollector.stopReadingInstance();
        dataCollector.stopListeningSensors(context);
    }

    private Classifier loadModel() {
        Classifier classifier = null;

        try {
            final String modelFile = "randomForest.model";
            classifier = (Classifier) weka.core.SerializationHelper.read(context.getAssets().open(modelFile));
        }
        catch (Exception e) {
            Log.d(getClass().getName(), "Error loading the model - " + e.getMessage());
            e.printStackTrace();
        }

        return classifier;
    }

    private Instances loadDataSet() {
        Instances dataSet = null;

        try {
            final String datasetFile = "aux-dataset.arff";
            ConverterUtils.DataSource ds = new ConverterUtils.DataSource(context.getAssets().open(datasetFile));
            dataSet = ds.getDataSet();

            final int classIndex = dataSet.attribute("activity-class").index();
            dataSet.setClassIndex(classIndex);
        }
        catch (Exception e) {
            Log.d(getClass().getName(), "Error loading the dataSet - " + e.getMessage());
            e.printStackTrace();
        }

        return dataSet;
    }

    private String predictClass(final ActivityReading reading) {
        final int SKIP_PREDICTION_MILLIS = 1000;
        boolean skipPrediction = System.currentTimeMillis() - lastLateralPrediction < SKIP_PREDICTION_MILLIS;

        final Instance unlabeledInstance = featureExtractor.toInstance(reading);

        try {
            dataSet.clear();
            unlabeledInstance.setDataset(dataSet);

            double prediction = classifier.classifyInstance(unlabeledInstance);
            String label = dataSet.classAttribute().value((int)prediction);

            if (label.equals(ActivityReading.JUMP_LEFT) || label.equals(ActivityReading.JUMP_RIGHT)) {
                label = skipPrediction ? ("skip " + label) : label;
                lastLateralPrediction = System.currentTimeMillis();
            }

            return label;
        }
        catch (Exception e) {
            Log.d(getClass().getName(), "Prediction ERROR - " + e.getMessage());
            e.printStackTrace();

            return "";
        }
    }

    private void showToast(String text) {
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        new Handler(Looper.getMainLooper())
            .postDelayed(toast::cancel, 600);
    }

    //#endregion
}
