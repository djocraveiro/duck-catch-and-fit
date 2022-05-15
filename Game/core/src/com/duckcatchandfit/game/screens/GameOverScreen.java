package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.duckcatchandfit.game.IGameNavigation;

public class GameOverScreen extends ScreenAdapter {

    //#region Fields

    // Navigation
    private final IGameNavigation gameNavigation;

    private final int score;

    // Graphics
    private final SpriteBatch batch;
    private final BitmapFont font;

    //#endregion

    //#region Initializers

    public GameOverScreen(IGameNavigation gameNavigation, int score) {
        this.gameNavigation = gameNavigation;
        this.score = score;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        float xPos = Gdx.graphics.getWidth() * 0.25f;

        font.draw(batch, "Game Over!",
                xPos, Gdx.graphics.getHeight() * 0.75f);

        font.draw(batch, "Score: " + score,
                xPos, Gdx.graphics.getHeight() * 0.5f);

        font.draw(batch, "Tap to restart.",
                xPos, Gdx.graphics.getHeight() * 0.25f);

        batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                dispose();
                gameNavigation.ShowGameScreen();

                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    //#endregion
}
