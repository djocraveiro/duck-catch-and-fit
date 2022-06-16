package com.duckcatchandfit.game;

import android.content.Context;
import com.duckcatchandfit.game.services.MovementDetector;

public class GameControllerAndroid implements IGameController {

    //#region Fields

    private final MovementDetector movementDetector;

    //#endregion

    //#region Initializers

    public GameControllerAndroid(Context context) {
         this.movementDetector = new MovementDetector(context);
    }

    //#endregion

    //#region Public Methods

    @Override
    public void register(final IGameControls gameControls) {
        movementDetector.startListening(gameControls);
    }

    @Override
    public void unregister(final IGameControls gameControls) {
        movementDetector.stopListening(gameControls);
    }

    public void dispose() {
        movementDetector.dispose();
    }

    //#endregion
}
