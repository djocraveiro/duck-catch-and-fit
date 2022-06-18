package com.duckcatchandfit.game;

import android.content.Context;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.duckcatchandfit.game.movement.services.MovementRecognizer;
import com.duckcatchandfit.game.voice.services.VoiceRecognizer;

public class GameControllerAndroid implements IGameController {

    //#region Fields

    private final MovementRecognizer movementRecognizer;
    private final VoiceRecognizer voiceRecognizer;
    private boolean voiceEnabled = false;

    //#endregion

    //#endregion

    public void setVoiceEnabled(boolean value) {
        this.voiceEnabled = value;
    }

    //#region

    //#region Initializers

    public GameControllerAndroid(Context context) {
         this.movementRecognizer = new MovementRecognizer(context);
         this.voiceRecognizer = new VoiceRecognizer(context);
    }

    //#endregion

    //#region Public Methods

    @Override
    public void register(final IGameControls gameControls) {
        movementRecognizer.startListening(gameControls);

        if (voiceEnabled) {
            voiceRecognizer.startListening(gameControls);
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
        movementRecognizer.stopListening(gameControls);

        if (voiceEnabled) {
            voiceRecognizer.stopListening(gameControls);
        }
        else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public void dispose() {
        movementRecognizer.dispose();
        voiceRecognizer.dispose();
    }

    //#endregion
}
