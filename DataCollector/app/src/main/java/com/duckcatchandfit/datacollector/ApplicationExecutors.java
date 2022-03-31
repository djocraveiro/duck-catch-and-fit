package com.duckcatchandfit.datacollector;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Src: https://rkay301.medium.com/android-programming-simple-concurrency-w-o-asynctask-or-rxjava-2899d568f849
public class ApplicationExecutors {

    //#region Fields

    private final Executor background;
    private final Executor mainThread;

    //#endregion

    //#region Properties

    public Executor getBackground() { return background; }

    public Executor getMainThread() { return mainThread; }

    //#endregion

    //#region Initializers

    public ApplicationExecutors() {
        this.background = Executors.newSingleThreadExecutor();
        this.mainThread = new MainThreadExecutor();
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(
                Looper.getMainLooper()
        );

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    //#endregion
}
