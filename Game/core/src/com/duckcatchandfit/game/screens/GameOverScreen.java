package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.duckcatchandfit.game.IGameNavigation;

public class GameOverScreen extends ScreenAdapter {

    //#region Fields

    // Navigation
    private IGameNavigation gameNavigation;

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
        font = loadFont();

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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();
        final float verticalMargin = font.getCapHeight() * 2.0f;

        batch.begin();

        font.draw(batch, "Game Over!",
                0, screenHeight * 0.8f,
                screenWidth, Align.center, false);

        font.draw(batch, "Score: " + score,
                0, screenHeight * 0.45f + verticalMargin,
                screenWidth, Align.center, false);

        font.draw(batch, "High Score: " + highScore,
                0, screenHeight * 0.5f - verticalMargin,
                screenWidth, Align.center, false);

        font.draw(batch, "Tap to play again",
                0, screenHeight * 0.2f,
                screenWidth, Align.center, false);

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
                gameNavigation.ShowGameScreen();

                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

        gameNavigation = null;
    }

    //#endregion

    //#region Private Methods

    private BitmapFont loadFont() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("LowballNeueExtraLight-vmBpL.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.5f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        BitmapFont font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        font.getData().setScale(0.5f);

        return font;
    }

    //#endregion
}
