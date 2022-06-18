package com.duckcatchandfit.game.voice.services;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.duckcatchandfit.game.IGameControls;
import com.duckcatchandfit.game.utils.ApplicationExecutors;

import java.util.List;

public class VoiceRecognizer implements SpeechDelegate {

    //#region Fields

    private final String FIRE_COMMAND = "fogo";

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private Context context;

    private final Speech speech;
    private IGameControls gameControls = null;
    private int originalVolumeLevel;

    //#endregion

    //#region Initializers

    public VoiceRecognizer(Context context) {
        this.context = context;

        speech = new Speech(context, context.getPackageName())
            .setPreferOffline(false) //TODO use offline in english
            .setPartialResults(false);
    }

    //#endregion

    //#region Public Methods

    public void startListening(final IGameControls gameControls) {
        this.gameControls = gameControls;

        //muteBeepSoundOfRecorder(true);

        exec.getMainThread().execute(() -> {
            try {
                speech.startListening(this);
            }
            catch (Exception ex) {
                Log.e(getClass().getName(), ex.getMessage());
            }
        });
    }

    public void stopListening(final IGameControls gameControls) {
        exec.getMainThread().execute(this::stopListeningVoice);

        muteBeepSoundOfRecorder(false);

        this.gameControls = null;
    }

    public void dispose() {
        exec.getMainThread().execute(this::stopListeningVoice);

        muteBeepSoundOfRecorder(false);

        context = null;
        gameControls = null;
    }

    //#region SpeechDelegate

    @Override
    public void onStartOfSpeech() {
        showToast("Speak now");
    }

    @Override
    public void onSpeechRmsChanged(float value) {
        //Log.i(getClass().getName(), "onSpeechRmsChanged: " + value);
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        if (results == null || results.isEmpty()) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        boolean fireCommand = false;

        for (String item: results) {
            builder.append(item)
                .append("|");

            if (item.equals(FIRE_COMMAND)) {
                fireCommand = true;
            }
        }

        showToast("PartialResults: " + builder.toString());

        if (fireCommand) {
            exec.getMainThread().execute(() -> {
                gameControls.getPlayerControls().fireLaser();
            });
        }
    }

    @Override
    public void onSpeechResult(String result) {
        if (result == null || result.isEmpty()) {
            return;
        }

        showToast("Result: " + result);

        if (result.equals(FIRE_COMMAND)) {
            exec.getMainThread().execute(() -> {
                gameControls.getPlayerControls().fireLaser();
            });
        }
    }

    @Override
    public void onStopOfSpeech() {
        /*if (speech.isListening()) {
            speech.stopListening();
        }
        else {*/
            try {
                speech.startListening(this);
            }
            catch (Exception ex) {
                Log.e(getClass().getName(), ex.getMessage());
            }
        //}
    }

    //#endregion

    //#region Private Methods

    private void muteBeepSoundOfRecorder(boolean mute) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                return;
            }

            if (mute) {
                originalVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            }
            else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_MUTE, originalVolumeLevel);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopListeningVoice() {
        try {
            speech.stopListening();
            speech.shutdown();
        }
        catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage());
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
