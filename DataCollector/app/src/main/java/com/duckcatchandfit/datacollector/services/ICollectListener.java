package com.duckcatchandfit.datacollector.services;

import com.duckcatchandfit.datacollector.models.ActivityReading;

public interface ICollectListener {
    void onCollectStop();
    void onInstanceCollected(ActivityReading reading);
}
