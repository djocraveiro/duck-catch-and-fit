package com.duckcatchandfit.game.voice.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//src: https://github.com/sachinvarma/Speech-Recognizer/blob/master/speech/src/main/java/com/sac/speech/Speech.java
public class SpeechCollector {

    //#region Fields

    private static final String LOG_TAG = SpeechCollector.class.getName();

    private SpeechRecognizer speechRecognizer;
    private boolean preferOffline = false;
    private boolean partialResults = true;
    private SpeechDelegate speechDelegate;
    private boolean isListening = false;

    private final List<String> partialData = new ArrayList<>();
    private String unstableData;

    private DelayedOperation delayedStopListening;
    private Context context;

    private Locale locale = Locale.getDefault();
    private long stopListeningDelayInMs = 10000;
    private long transitionMinimumDelay = 1200;
    private long lastActionTimestamp;
    private List<String> lastPartialResults = null;

    private final RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(final Bundle bundle) {
            partialData.clear();
            unstableData = null;
        }

        @Override
        public void onBeginningOfSpeech() {
            delayedStopListening.start(new DelayedOperation.Operation() {
                @Override
                public void onDelayedOperation() {
                    returnPartialResults();

                    onStopOfSpeech();
                }

                @Override
                public boolean shouldExecuteDelayedOperation() {
                    return true;
                }
            });
        }

        @Override
        public void onRmsChanged(final float v) {
            try {
                if (speechDelegate != null)
                    speechDelegate.onSpeechRmsChanged(v);
            }
            catch (final Throwable exc) {
                Log.e(LOG_TAG, "Unhandled exception in delegate onSpeechRmsChanged", exc);
            }
        }

        @Override
        public void onPartialResults(final Bundle bundle) {
            delayedStopListening.resetTimer();

            final List<String> partialResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final List<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");

            if (partialResults != null && !partialResults.isEmpty()) {
                partialData.clear();
                partialData.addAll(partialResults);
                SpeechCollector.this.unstableData = unstableData != null && !unstableData.isEmpty()
                        ? unstableData.get(0) : null;
                try {
                    if (lastPartialResults == null || !lastPartialResults.equals(partialResults)) {
                        if (speechDelegate != null) {
                            speechDelegate.onSpeechPartialResults(partialResults);
                        }

                        lastPartialResults = partialResults;
                    }
                }
                catch (final Throwable exc) {
                    Log.e(LOG_TAG, "Unhandled exception in delegate onSpeechPartialResults", exc);
                }
            }
        }

        @Override
        public void onResults(final Bundle bundle) {
            delayedStopListening.cancel();

            final List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final String result;

            if (results != null && !results.isEmpty()
                && results.get(0) != null && !results.get(0).isEmpty()) {

                result = results.get(0);
            }
            else {
                Log.i(LOG_TAG, "No speech results, getting partial");
                result = getPartialResultsAsString();
            }

            isListening = false;

            try {
                if (speechDelegate != null) {
                    speechDelegate.onSpeechResult(result.trim());
                }
            }
            catch (final Throwable exc) {
                Log.e(LOG_TAG, "Unhandled exception in delegate onSpeechResult", exc);
            }

            initSpeechRecognizer(context);

            onStopOfSpeech();
        }

        @Override
        public void onError(final int code) {
            Log.e(LOG_TAG, "Speech recognition error", new SpeechRecognitionException(code));

            returnPartialResults();

            onStopOfSpeech();
        }

        @Override
        public void onBufferReceived(final byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onEvent(final int i, final Bundle bundle) {

        }
    };

    //#endregion

    //#region Properties

    public boolean isListening() {
        return isListening;
    }

    public SpeechCollector setLocale(final Locale locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Set whether to only use an offline speech recognition engine.
     * The default is false, meaning that either network or offline recognition engines may be used.
     *
     * @param value true to prefer offline engine, false to use either one of the two
     * @return speech instance
     */
    public SpeechCollector setPreferOffline(final boolean value) {
        preferOffline = value;
        return this;
    }

    /**
     * Set whether partial results should be returned by the recognizer as the user speaks
     * (default is true). The server may ignore a request for partial results in some or all cases.
     *
     * @param value true to get also partial recognition results, false otherwise
     * @return speech instance
     */
    public SpeechCollector setPartialResults(final boolean value) {
        partialResults = value;
        return this;
    }

    /**
     * Sets the idle timeout after which the listening will be automatically stopped.
     *
     * @param milliseconds timeout in milliseconds
     * @return speech instance
     */
    public SpeechCollector setStopListeningAfterInactivity(final long milliseconds) {
        stopListeningDelayInMs = milliseconds;
        initDelayedStopListening(context);
        return this;
    }

    /**
     * Sets the minimum interval between start/stop events. This is useful to prevent
     * monkey input from users.
     *
     * @param milliseconds minimum interval between state change in milliseconds
     * @return speech instance
     */
    public SpeechCollector setTransitionMinimumDelay(final long milliseconds) {
        transitionMinimumDelay = milliseconds;
        return this;
    }

    //#endregion

    //#region

    public synchronized void startListening(final Context context, final SpeechDelegate delegate)
        throws SpeechRecognitionNotAvailable, GoogleVoiceTypingDisabledException {

        if (isListening) {
            throw new IllegalStateException("already listening");
        }

        if (context == null) {
            throw new IllegalArgumentException("context must be defined");
        }

        if (delegate == null) {
            throw new IllegalArgumentException("delegate must be defined");
        }

        this.context = context;
        speechDelegate = delegate;

        initSpeechRecognizer(context);
        if (speechRecognizer == null) {
            throw new SpeechRecognitionNotAvailable();
        }

        if (throttleAction()) {
            Log.d(LOG_TAG, "Hey man calm down! Throttling start to prevent disaster!");
            return;
        }

        final Intent intent = buildSpeechRecognizerIntent();

        try {
            speechRecognizer.startListening(intent);
        }
        catch (final SecurityException exc) {
            throw new GoogleVoiceTypingDisabledException();
        }

        isListening = true;
        updateLastActionTimestamp();

        try {
            if (speechDelegate != null) {
                speechDelegate.onStartOfSpeech();
            }
        }
        catch (final Throwable exc) {
            Log.e(LOG_TAG, "Unhandled exception in delegate onStartOfSpeech", exc);
        }
    }

    public synchronized void stopListening() {
        if (speechRecognizer != null) {
            try {

                speechRecognizer.stopListening();
                speechRecognizer.destroy();
            }
            catch( final Exception ex){
                Log.e(LOG_TAG, "While stopping the speech recognizer", ex);
            }
            finally {
                speechRecognizer = null;
            }
        }

        context = null;
        speechDelegate = null;
        isListening = false;
        updateLastActionTimestamp();
    }

    //#endregion

    private void initSpeechRecognizer(final Context context) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            if (speechRecognizer != null) {
                try {
                    speechRecognizer.destroy();
                }
                catch (final Throwable exc) {
                    Log.d(LOG_TAG, "Non-Fatal error while destroying speech. " + exc.getMessage());
                }
                finally {
                    speechRecognizer = null;
                }
            }

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(recognitionListener);

            initDelayedStopListening(context);
        }
        else {
            speechRecognizer = null;
        }

        partialData.clear();
        unstableData = null;
    }

    private Intent buildSpeechRecognizerIntent() {
        return new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, partialResults)
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.getLanguage())
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            .putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, preferOffline)
            .putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
    }

    private void initDelayedStopListening(final Context context) {
        if (delayedStopListening != null) {
            delayedStopListening.cancel();
            delayedStopListening = null;
        }

        delayedStopListening = new DelayedOperation(context, "delayStopListening", stopListeningDelayInMs);
    }

    private void updateLastActionTimestamp() {
        lastActionTimestamp = System.currentTimeMillis();
    }

    private boolean throttleAction() {
        return (new Date().getTime() <= (lastActionTimestamp + transitionMinimumDelay));
    }

    private void returnPartialResults() {
        isListening = false;

        try {
            if (speechDelegate != null) {
                speechDelegate.onSpeechResult(getPartialResultsAsString());
            }
        }
        catch (final Throwable ex) {
            Log.e(LOG_TAG, "Unhandled exception in delegate onSpeechResult", ex);
        }
    }

    private void onStopOfSpeech() {
        try {
            if (speechDelegate != null) {
                speechDelegate.onStopOfSpeech();
            }
        }
        catch (final Throwable ex) {
            Log.e(LOG_TAG, "Unhandled exception in delegate onStopOfSpeech", ex);
        }
    }

    private String getPartialResultsAsString() {
        final StringBuilder out = new StringBuilder("");

        for (final String partial : partialData) {
            out.append(partial).append(" ");
        }

        if (unstableData != null && !unstableData.isEmpty()) {
            out.append(unstableData);
        }

        return out.toString().trim();
    }
}
