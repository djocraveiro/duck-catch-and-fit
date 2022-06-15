package com.duckcatchandfit.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.duckcatchandfit.game.commands.CommandIntentHelper;
import com.duckcatchandfit.game.models.ActivityReading;
import com.duckcatchandfit.game.services.DataCollector;
import com.duckcatchandfit.game.services.FeatureExtractor;
import com.duckcatchandfit.game.services.ICollectListener;
import com.duckcatchandfit.game.utils.ApplicationExecutors;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class ActionResolverAndroid implements IActionResolver, ICollectListener {

    //#region Fields

    public static final int REQUEST_OK = 1;
    private final String language;

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private Context context;

    private final DataCollector dataCollector = new DataCollector();
    private final FeatureExtractor featureExtractor = new FeatureExtractor();
    private Classifier classifier;
    private Instances dataSet;
    private long lastLateralPrediction = 0;
    private String lastActivity = "";
    private IGameControls gameControls;

    //#endregion

    //#region

    public void setGameControls(IGameControls gameControls) {
        this.gameControls = gameControls;
    }

    //#endregion

    //#region Initializers

    public ActionResolverAndroid(Context context) {
        this.context = context;
        this.language = "en-US";
        this.classifier = loadModel();
        this.dataSet = loadDataSet();

        dataCollector.setCollectListener(this);
    }

    //#endregion

    //#region Public Methods

    //#region IActionResolver

    @Override
    public void startListeningSensors() {
        dataCollector.startListeningSensors(context);
        dataCollector.startReadingInstance(DataCollector.COLLECT_MODE_INSTANCE_LOOP);
    }

    @Override
    public void stopListeningSensors() {
        dataCollector.stopReadingInstance();
        dataCollector.stopListeningSensors(context);
    }

    @Override
    public void showToast(final CharSequence toastMessage, final int toastDuration) {
        exec.getMainThread().execute(() -> {
            Toast.makeText(context, toastMessage, toastDuration).show();
        });
    }

    @Override
    public void showSpeechPopup() {
        exec.getMainThread().execute(() -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);

            try {
                // We open the Speech dialog here using a request code
                // and retrieve the spoken text in AndroidLauncher's onActivityResult().
                ((Activity) context).startActivityForResult(intent, REQUEST_OK);
            }
            catch (Exception e) {
                showToast(e.toString(), 5000);
                Gdx.app.log(ActionResolverAndroid.class.getName(),
                        "error initializing speech engine" + e);
            }
        });
    }

    //#endregion

    //#region ICollectListener

    @Override
    public void onCollectStop() {
        Gdx.app.log(this.getClass().getName(), "onCollectStop");
    }

    @Override
    public void onInstanceCollected(final ActivityReading reading) {
        final String activityLabel = predictClass(reading);
        if (activityLabel.isEmpty() || lastActivity.equals(activityLabel)) {
            return;
        }

        lastActivity = activityLabel;

        //Intent intent = CommandIntentHelper.createIntent(activityLabel);
        //context.sendBroadcast(intent);

        ICommand command = CommandIntentHelper.readCommand(activityLabel);
        if (command != null) {
            exec.getMainThread().execute(() -> {
                Toast.makeText(context, activityLabel, Toast.LENGTH_SHORT).show();
                gameControls.sendCommand(command);
            });
        }
    }

    public void dispose() {
        stopListeningSensors();
        context = null;
        classifier = null;
        dataSet = null;
    }

    //#endregion

    //#region Private Methods

    private Classifier loadModel() {
        Classifier classifier = null;

        try {
            final String modelFile = "randomForest.model";
            classifier = (Classifier) weka.core.SerializationHelper.read(context.getAssets().open(modelFile));
        }
        catch (Exception e) {
            Gdx.app.log(this.getClass().getName(), "Error loading the model - " + e.getMessage());
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
            Gdx.app.log(this.getClass().getName(), "Error loading the dataSet - " + e.getMessage());
            e.printStackTrace();
        }

        return dataSet;
    }

    private String predictClass(final ActivityReading reading) {
        final int SKIP_PREDICTION_MILLIS = 800;
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
            Gdx.app.log(this.getClass().getName(), "Prediction ERROR - " + e.getMessage());
            e.printStackTrace();

            return "";
        }
    }

    //#endregion
}
