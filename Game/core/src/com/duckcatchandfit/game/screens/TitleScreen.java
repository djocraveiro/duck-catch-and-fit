package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.duckcatchandfit.game.IGameNavigation;

public class TitleScreen implements Screen {

    //#region Fields

    // Navigation
    private IGameNavigation gameNavigation;

    // Screen
    private final Camera camera;
    private final Viewport viewport;

    // Graphics
    private final SpriteBatch batch;
    private final Texture logoTexture;
    private final BitmapFont font;

    //#endregion

    //#region Initializers

    public TitleScreen(IGameNavigation gameNavigation) {
        this.gameNavigation = gameNavigation;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(72, 128, camera);

        batch = new SpriteBatch();
        logoTexture = new Texture("duck-catch-and-fit.png");
        font = loadFont();
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final float screenWidth = viewport.getWorldWidth();
        final float screenHeight = viewport.getWorldHeight();

        batch.begin();

        font.getData().setScale(0.14f);
        font.draw(batch, "Duck Catch And Fit",
                0, screenHeight * 0.8f,
                screenWidth, Align.center, false);

        final float logoSize = 48;
        final float logoXOffset = logoSize / 2.0f;
        batch.draw(logoTexture,
                screenWidth * 0.5f - logoXOffset,
                screenHeight * 0.5f - logoXOffset,
                logoSize, logoSize);

        font.getData().setScale(0.08f);
        font.draw(batch, "Tap to play",
                0, screenHeight * 0.2f,
                screenWidth, Align.center, false);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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
        logoTexture.dispose();
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

        return font;
    }

    //#endregion
}
