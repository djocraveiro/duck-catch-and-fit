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
import java.util.Locale;

public class VoiceController {

    //#region Fields

    private final ApplicationExecutors exec = new ApplicationExecutors();
    private Context context;

    private final SpeechCollector speech;
    private IGameControls gameControls = null;
    private int originalVolumeLevel;

    private final SpeechDelegate speechDelegate = new SpeechDelegate() {
        @Override
        public void onStartOfSpeech() {

        }

        @Override
        public void onSpeechRmsChanged(float value) {

        }

        @Override
        public void onSpeechPartialResults(List<String> results) {
            if (results == null || results.isEmpty()) {
                return;
            }

            for (String partialResult : results) {
                if (partialResult == null || partialResult.isEmpty()) {
                    continue;
                }

                if (handleCommand(partialResult)) {
                    break;
                }
            }
        }

        @Override
        public void onSpeechResult(String result) {
            if (result == null || result.isEmpty()) {
                return;
            }

            handleCommand(result);
        }

        @Override
        public void onStopOfSpeech() {
            try {
                // start another collect cycle
                speech.startListening(context, this);
            }
            catch (Exception ex) {
                Log.e(getClass().getName(), ex.getMessage());
            }
        }
    };

    //#endregion

    //#region Initializers

    public VoiceController(Context context) {
        this.context = context;

        speech = new SpeechCollector()
            .setPreferOffline(false)
            .setLocale(Locale.forLanguageTag("en-US"))
            .setPartialResults(true);
    }

    //#endregion

    //#region Public Methods

    public void startListening(final IGameControls gameControls) {
        this.gameControls = gameControls;

        exec.getMainThread().execute(() -> {
            muteBeepSoundOfRecorder(true);

            try {
                speech.startListening(context, speechDelegate);
            }
            catch (Exception ex) {
                Log.e(getClass().getName(), ex.getMessage());
            }
        });
    }

    public void stopListening(final IGameControls gameControls) {
        exec.getMainThread().execute(this::stopListeningVoice);

        this.gameControls = null;
    }

    public void dispose() {
        exec.getMainThread().execute(this::stopListeningVoice);

        context = null;
        gameControls = null;
    }

    //#region Private Methods

    private void muteBeepSoundOfRecorder(boolean mute) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                return;
            }

            if (mute) {
                originalVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_MUTE, 0);
            }
            else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_UNMUTE, originalVolumeLevel);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopListeningVoice() {
        try {
            speech.stopListening();
        }
        catch (Exception ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }

        muteBeepSoundOfRecorder(false);
    }

    private boolean handleCommand(final String command) {
        //showToast(command);

        final String FIRE_COMMAND = "fire";

        if (command.equals(FIRE_COMMAND)) {
            exec.getMainThread().execute(() -> {
                if (gameControls != null) {
                    gameControls.getPlayerControls().fireLaser();
                }
            });

            return true;
        }

        return false;
    }

    private void showToast(final String text) {
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        new Handler(Looper.getMainLooper())
                .postDelayed(toast::cancel, 600);
    }

    //#endregion
}
