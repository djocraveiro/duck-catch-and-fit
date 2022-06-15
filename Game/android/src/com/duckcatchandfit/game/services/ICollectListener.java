package com.duckcatchandfit.game.services;

import com.duckcatchandfit.game.models.ActivityReading;

public interface ICollectListener {
    void onCollectStop();

    void onInstanceCollected(ActivityReading reading);
}
