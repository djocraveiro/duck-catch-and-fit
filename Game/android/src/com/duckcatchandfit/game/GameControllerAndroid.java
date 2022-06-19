package com.duckcatchandfit.game;

import android.content.Context;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.duckcatchandfit.game.movement.services.MovementController;
import com.duckcatchandfit.game.voice.services.VoiceController;

public class GameControllerAndroid implements IGameController {

    //#region Fields

    private final MovementController movementController;
    private final VoiceController voiceController;
    private boolean voiceEnabled = false;

    //#endregion

    //#endregion

    public void setVoiceEnabled(boolean value) {
        this.voiceEnabled = value;
    }

    //#region

    //#region Initializers

    public GameControllerAndroid(Context context) {
         this.movementController = new MovementController(context);
         this.voiceController = new VoiceController(context);
    }

    //#endregion

    //#region Public Methods

    @Override
    public void register(final IGameControls gameControls) {
        movementController.startListening(gameControls);

        if (voiceEnabled) {
            voiceController.startListening(gameControls);
        }
        else {
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    gameControls.getPlayerControls().fireLaser();
                    return true;
                }
            });
        }
    }

    @Override
    public void unregister(final IGameControls gameControls) {
        movementController.stopListening(gameControls);

        if (voiceEnabled) {
            voiceController.stopListening(gameControls);
        }
        else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public void dispose() {
        movementController.dispose();
        voiceController.dispose();
    }

    //#endregion
}
