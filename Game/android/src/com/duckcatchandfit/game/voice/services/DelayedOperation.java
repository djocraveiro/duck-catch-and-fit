package com.duckcatchandfit.game.voice.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class DelayedOperation {

    //#region Fields

    private static final String LOG_TAG = DelayedOperation.class.getName();

    public interface Operation {
        void onDelayedOperation();
        boolean shouldExecuteDelayedOperation();
    }

    private final long delay;
    private Operation operation;
    private Timer timer;
    private boolean started;
    private Context context;
    private final String tag;

    //#endregion

    //#region Initializers

    public DelayedOperation(Context context, String tag, long delayInMilliseconds) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        }

        if (delayInMilliseconds <= 0) {
            throw new IllegalArgumentException("The delay in milliseconds must be > 0");
        }

        this.context = context;
        this.tag = tag;
        delay = delayInMilliseconds;

        Log.d(LOG_TAG, "created delayed operation with tag: " + this.tag);
    }

    //#endregion

    //#region Public Methods

    public void start(final Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("The operation must be defined!");
        }

        Log.d(LOG_TAG, "starting delayed operation with tag: " + tag);

        this.operation = operation;
        cancel();
        started = true;
        resetTimer();
    }

    public void resetTimer() {
        if (!started) {
            return;
        }

        if (timer != null) {
            timer.cancel();
        }

        Log.d(LOG_TAG, "resetting delayed operation with tag: " + tag);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (operation.shouldExecuteDelayedOperation()) {
                    Log.d(LOG_TAG, "executing delayed operation with tag: " + tag);

                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            operation.onDelayedOperation();
                        }
                    });
                }

                cancel();
            }
        }, delay);
    }

    public void cancel() {
        if (timer != null) {
            Log.d(LOG_TAG, "cancelled delayed operation with tag: " + tag);

            timer.cancel();
            timer = null;
        }

        started = false;
        context = null;
    }

    //#endregion
}