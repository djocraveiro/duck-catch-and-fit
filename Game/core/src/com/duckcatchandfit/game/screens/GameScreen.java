package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.duckcatchandfit.game.IGameNavigation;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.duckcatch.DuckCatchEngine;
import com.duckcatchandfit.game.players.PlayerEngine;
import com.duckcatchandfit.game.obstacles.ObstacleEngine;

import java.util.Locale;

public class GameScreen implements Screen {

    //#region Fields

    // navigation
    private IGameNavigation gameNavigation;

    // Screen
    private final Camera camera;
    private final Viewport viewport;

    // Graphics
    private final SpriteBatch batch;
    private final Texture background;
    private final HeadsUpDisplay headsUpDisplay;

    // Timing
    private float backgroundOffset = 0.0f;
    private final float backgroundScrollingSpeed;

    // World parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    // Game objects
    private final ObstacleEngine obstacleEngine;
    private final PlayerEngine playerEngine;
    private final DuckCatchEngine duckCatchEngine;
    private int score = 0;

    //#endregion

    //#region Initializers

    public GameScreen(IGameNavigation gameNavigation) {
        this.gameNavigation = gameNavigation;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        background = new Texture("grass.png");
        backgroundScrollingSpeed = viewport.getWorldHeight() / 6.0f;

        Rectangle worldBoundingBox = new Rectangle(
                viewport.getScreenX(), viewport.getScreenY(), WORLD_WIDTH, WORLD_HEIGHT);

        WorldMatrix worldMatrix = new WorldMatrix(16, 16, worldBoundingBox);

        obstacleEngine = new ObstacleEngine(worldMatrix, 2.2f,
                new Texture("tree.png"),
                new Texture("rock.png"));

        playerEngine = new PlayerEngine(worldMatrix,
                new Texture("character.png"));

        duckCatchEngine = new DuckCatchEngine();

        batch = new SpriteBatch();

        headsUpDisplay = new HeadsUpDisplay(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);

        playerEngine.renderPlayer(deltaTime, backgroundScrollingSpeed, batch);

        boolean endGame = obstacleEngine.detectCollisions(playerEngine.getPlayer());
        if (endGame) {
            batch.end();
            gameNavigation.ShowGameOverScreen(score);

            return;
        }

        obstacleEngine.addObstacles(deltaTime);
        int skippedObstacles = obstacleEngine.renderObstacles(deltaTime, backgroundScrollingSpeed, batch);
        score += skippedObstacles;

        headsUpDisplay.renderHeadsUpDisplay(score, batch);

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
                final float screenXCenter = viewport.getScreenWidth() / 2.0f;

                if (screenX < screenXCenter) {
                    playerEngine.movePlayerLeft();
                }
                else {
                    playerEngine.movePlayerRight();
                }

                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    playerEngine.movePlayerLeft();
                }
                else if (keycode == Input.Keys.RIGHT) {
                    playerEngine.movePlayerRight();
                }

                return true;
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        headsUpDisplay.dispose();
        obstacleEngine.dispose();
        playerEngine.dispose();
        duckCatchEngine.dispose();

        gameNavigation = null;
    }

    //#endregion

    //#region Private Methods

    private void renderBackground(float deltaTime) {
        backgroundOffset += deltaTime * backgroundScrollingSpeed;

        if (backgroundOffset > WORLD_HEIGHT) {
            backgroundOffset = 0;
        }

        batch.draw(background,
                0, -backgroundOffset,
                WORLD_WIDTH, WORLD_HEIGHT);

        batch.draw(background,
                0, -backgroundOffset + WORLD_HEIGHT,
                WORLD_WIDTH, WORLD_HEIGHT);
    }

    //#endregion
}
