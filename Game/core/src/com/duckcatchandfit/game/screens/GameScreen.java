package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.duckcatchandfit.game.IGameNavigation;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.players.PlayerEngine;
import com.duckcatchandfit.game.obstacles.ObstacleEngine;

public class GameScreen implements Screen {

    //#region Fields

    // navigation
    private final IGameNavigation gameNavigation;

    // Screen
    private final Camera camera;
    private final Viewport viewport;

    // Graphics
    private final SpriteBatch batch;
    private final Texture background;

    // Timing
    private float backgroundOffset = 0.0f;
    private final float backgroundScrollingSpeed;

    // World parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    // Game objects
    private final ObstacleEngine obstacleEngine;
    private final PlayerEngine playerEngine;

    //#endregion

    //#region Initializers

    public GameScreen(IGameNavigation gameNavigation) {
        this.gameNavigation = gameNavigation;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        background = new Texture("grass.png");
        backgroundScrollingSpeed = viewport.getWorldHeight() / 8.0f;

        Rectangle worldBoundingBox = new Rectangle(
                viewport.getScreenX(), viewport.getScreenY(), WORLD_WIDTH, WORLD_HEIGHT);

        WorldMatrix worldMatrix = new WorldMatrix(16, 16, worldBoundingBox);

        obstacleEngine = new ObstacleEngine(worldMatrix, 2.2f,
                new Texture("tree.png"),
                new Texture("rock.png"));

        playerEngine = new PlayerEngine(worldMatrix,
                new Texture("character.png"));

        batch = new SpriteBatch();
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);

        detectInput(deltaTime);

        playerEngine.renderPlayer(deltaTime, backgroundScrollingSpeed, batch);

        obstacleEngine.addObstacles(deltaTime);
        obstacleEngine.renderObstacles(deltaTime, backgroundScrollingSpeed, batch);

        boolean endGame = obstacleEngine.detectCollisions(playerEngine.getPlayer());

        batch.end();

        if (endGame) {
            dispose();
            gameNavigation.ShowGameOverScreen(0); //TODO
        }
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

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        obstacleEngine.dispose();
        playerEngine.dispose();
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

    private void detectInput(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            playerEngine.movePlayerLeft();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            playerEngine.movePlayerRight();
        }
    }

    //#endregion
}
