package com.duckcatchandfit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameControllerDesktop implements IGameController {

    //#region Public Methods

    @Override
    public void register(final IGameControls gameControls) {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                final float screenXCenter = gameControls.getScreenWidth() / 2.0f;

                if (screenX < screenXCenter) {
                    gameControls.getPlayerControls().movePlayerLeft();
                }
                else {
                    gameControls.getPlayerControls().movePlayerRight();
                }

                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    gameControls.getPlayerControls().movePlayerLeft();
                }
                else if (keycode == Input.Keys.RIGHT) {
                    gameControls.getPlayerControls().movePlayerRight();
                }
                else if (keycode == Input.Keys.SPACE) {
                    gameControls.getPlayerControls().fireLaser();
                }

                return true;
            }
        });
    }

    @Override
    public void unregister(final IGameControls gameControls) {
        Gdx.input.setInputProcessor(null);
    }

    //#endregion
}
