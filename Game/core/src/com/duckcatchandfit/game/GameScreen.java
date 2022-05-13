package com.duckcatchandfit.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.duckcatchandfit.game.obstacles.IObstacle;
import com.duckcatchandfit.game.obstacles.Obstacle;
import com.duckcatchandfit.game.obstacles.ObstacleEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameScreen implements Screen {

    //#region Fields

    // Screen
    private final Camera camera;
    private final Viewport viewport;

    // Graphics
    private final SpriteBatch batch;
    private final Texture background;

    // Timing
    private float backgroundOffset;
    private final float backgroundScrollingSpeed;

    // World parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    // Game objects
    private final ObstacleEngine obstacleEngine;

    //#endregion

    //#region Initializers

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        background = new Texture("grass.png");
        backgroundOffset = 0.0f;
        backgroundScrollingSpeed = (float)viewport.getWorldHeight() / 8.0f;

        Rectangle worldBoundingBox = new Rectangle(
                viewport.getScreenX(), viewport.getScreenY(), WORLD_WIDTH, WORLD_HEIGHT);

        obstacleEngine = new ObstacleEngine(worldBoundingBox, 2f);

        batch = new SpriteBatch();
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);

        obstacleEngine.addObstacles(deltaTime);
        obstacleEngine.renderObstacles(deltaTime, backgroundScrollingSpeed, batch);

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

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

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
