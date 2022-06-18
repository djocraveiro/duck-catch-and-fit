package com.duckcatchandfit.game.voice.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper class to easily work with Android speech recognition.
 *
 * @author Sachin Varma
 */
public class Speech {

    private static final String LOG_TAG = Speech.class.getSimpleName();

    private SpeechRecognizer mSpeechRecognizer;
    private final String mCallingPackage;
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
                    returnPartialResultsAndRecreateSpeechRecognizer();
                    Log.d("ReachedStop", "Stoppong");
                    //  mListenerDelay.onClick("1");
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
                Log.e(Speech.class.getSimpleName(),
                        "Unhandled exception in delegate onSpeechRmsChanged", exc);
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
                Speech.this.unstableData = unstableData != null && !unstableData.isEmpty()
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
                    Log.e(Speech.class.getSimpleName(),
                            "Unhandled exception in delegate onSpeechPartialResults", exc);
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
                Log.i(Speech.class.getSimpleName(), "No speech results, getting partial");
                result = getPartialResultsAsString();
            }

            isListening = false;

            try {
                if (speechDelegate != null)
                    speechDelegate.onSpeechResult(result.trim());
            }
            catch (final Throwable exc) {
                Log.e("Speech", "Unhandled exception in delegate onSpeechResult", exc);
            }

            initSpeechRecognizer(context);
        }

        @Override
        public void onError(final int code) {
            Log.e(LOG_TAG, "Speech recognition error", new SpeechRecognitionException(code));
            returnPartialResultsAndRecreateSpeechRecognizer();
        }

        @Override
        public void onBufferReceived(final byte[] bytes) {
            Log.i("Speech", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i("Speech", "onEndOfSpeech");
        }

        @Override
        public void onEvent(final int i, final Bundle bundle) {
            Log.i("Speech", "onEvent");
        }
    };

    //#region Properties

    public boolean isListening() {
        return isListening;
    }

    public Speech setLocale(final Locale locale) {
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
    public Speech setPreferOffline(final boolean value) {
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
    public Speech setPartialResults(final boolean value) {
        partialResults = value;
        return this;
    }

    /**
     * Sets the idle timeout after which the listening will be automatically stopped.
     *
     * @param milliseconds timeout in milliseconds
     * @return speech instance
     */
    public Speech setStopListeningAfterInactivity(final long milliseconds) {
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
    public Speech setTransitionMinimumDelay(final long milliseconds) {
        transitionMinimumDelay = milliseconds;
        return this;
    }

    //#endregion

    //#region Initializers

    /**
     * Initializes speech recognition.
     *
     * @param context        application context
     * @param callingPackage The extra key used in an intent to the speech recognizer for
     *                       voice search. Not generally to be used by developers.
     *                       The system search dialog uses this, for example, to set a calling
     *                       package for identification by a voice search API.
     *                       If this extra is set by anyone but the system process,
     *                       it should be overridden by the voice search implementation.
     *                       By passing null or empty string (which is the default) you are
     *                       not overriding the calling package
     */
    public Speech(final Context context, final String callingPackage) {
        initSpeechRecognizer(context);
        mCallingPackage = callingPackage;
    }

    //#endregion

    //#region

    /**
     * Starts voice recognition.
     *
     * @param delegate     delegate which will receive speech recognition events and status
     * @throws SpeechRecognitionNotAvailable      when speech recognition is not available on the device
     * @throws GoogleVoiceTypingDisabledException when google voice typing is disabled on the device
     */
    public void startListening(final SpeechDelegate delegate)
            throws SpeechRecognitionNotAvailable, GoogleVoiceTypingDisabledException {
        if (isListening) {
            return;
        }

        if (mSpeechRecognizer == null) {
            throw new SpeechRecognitionNotAvailable();
        }

        if (delegate == null) {
            throw new IllegalArgumentException("delegate must be defined!");
        }

        if (throttleAction()) {
            Log.d(getClass().getName(), "Hey man calm down! Throttling start to prevent disaster!");
            return;
        }

        speechDelegate = delegate;

        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, partialResults)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.getLanguage())
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, preferOffline);

        if (mCallingPackage != null && !mCallingPackage.isEmpty()) {
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mCallingPackage);
        }

        try {
            mSpeechRecognizer.startListening(intent);
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
            Log.e(getClass().getName(), "Unhandled exception in delegate onStartOfSpeech", exc);
        }
    }

    /**
     * Stops voice recognition listening.
     * This method does nothing if voice listening is not active
     */
    public void stopListening() {
        if (!isListening) {
            return;
        }

        if (throttleAction()) {
            Log.d(getClass().getSimpleName(), "Hey man calm down! Throttling stop to prevent disaster!");
            return;
        }

        isListening = false;
        updateLastActionTimestamp();
        returnPartialResultsAndRecreateSpeechRecognizer();
    }

    /**
     * Must be called inside Activity's onDestroy.
     */
    public synchronized void shutdown() {
        if (mSpeechRecognizer != null) {
            try {
                mSpeechRecognizer.stopListening();
            }
            catch (final Exception exc) {
                Log.e(getClass().getName(), "Warning while de-initing speech recognizer", exc);
            }
        }

        speechDelegate = null;
    }

    //#endregion

    private void initSpeechRecognizer(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must be defined!");
        }

        this.context = context;

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            if (mSpeechRecognizer != null) {
                try {
                    mSpeechRecognizer.destroy();
                }
                catch (final Throwable exc) {
                    Log.d(getClass().getName(), "Non-Fatal error while destroying speech. " + exc.getMessage());
                }
                finally {
                    mSpeechRecognizer = null;
                }
            }

            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mSpeechRecognizer.setRecognitionListener(recognitionListener);
            initDelayedStopListening(context);
        }
        else {
            mSpeechRecognizer = null;
        }

        partialData.clear();
        unstableData = null;
    }

    private void initDelayedStopListening(final Context context) {
        if (delayedStopListening != null) {
            delayedStopListening.cancel();
            delayedStopListening = null;
        }

        if (speechDelegate != null) {
            speechDelegate.onStopOfSpeech();
        }

        delayedStopListening = new DelayedOperation(context, "delayStopListening", stopListeningDelayInMs);
    }

    private void updateLastActionTimestamp() {
        lastActionTimestamp = new Date().getTime();
    }

    private boolean throttleAction() {
        return (new Date().getTime() <= (lastActionTimestamp + transitionMinimumDelay));
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

    private void returnPartialResultsAndRecreateSpeechRecognizer() {
        isListening = false;

        try {
            if (speechDelegate != null && partialResults) {
                speechDelegate.onSpeechResult(getPartialResultsAsString());
            }
        }
        catch (final Throwable exc) {
            Log.e(Speech.class.getSimpleName(), "Unhandled exception in delegate onSpeechResult", exc);
        }

        // recreate the speech recognizer
        initSpeechRecognizer(context);
    }

}