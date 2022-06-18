package com.duckcatchandfit.game.movement.services;

import com.duckcatchandfit.game.movement.models.ActivityReading;

public interface ICollectListener {
    void onCollectStop();

    void onInstanceCollected(ActivityReading reading);
}
