package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.duckcatchandfit.game.IGameNavigation;

public class GameOverScreen extends ScreenAdapter {

    //#region Fields

    // Navigation
    private final IGameNavigation gameNavigation;

    // Scores
    private final int score;
    private int highScore;

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

        String HIGH_SCORE_KEY = "high-score";
        Preferences pref = Gdx.app.getPreferences("duck-catch-and-fit");
        highScore = pref.getInteger(HIGH_SCORE_KEY, 0);

        if (score > highScore) {
            highScore = score;
            pref.putInteger(HIGH_SCORE_KEY, highScore);
            pref.flush();
        }
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

        font.draw(batch, "high Score: " + highScore,
                xPos, Gdx.graphics.getHeight() * 0.45f);

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
