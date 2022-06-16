package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.duckcatchandfit.game.*;
import com.duckcatchandfit.game.ducks.DuckEngine;
import com.duckcatchandfit.game.ducks.IDuck;
import com.duckcatchandfit.game.obstacles.IObstacle;
import com.duckcatchandfit.game.players.ILaser;
import com.duckcatchandfit.game.players.IPlayer;
import com.duckcatchandfit.game.players.IPlayerControls;
import com.duckcatchandfit.game.players.PlayerEngine;
import com.duckcatchandfit.game.obstacles.ObstacleEngine;
import com.duckcatchandfit.game.screens.components.HeadsUpDisplay;

import java.util.List;
import java.util.ListIterator;

public class GameScreen implements Screen, IGameControls {

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
    private IGameController gameController;
    private final ObstacleEngine obstacleEngine;
    private final PlayerEngine playerEngine;
    private final DuckEngine duckEngine;
    private int score = 0;

    //#endregion

    //#region Initializers

    public GameScreen(IGameNavigation gameNavigation, IGameController gameController) {
        this.gameNavigation = gameNavigation;
        this.gameController = gameController;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        background = new Texture("grass.png");
        backgroundScrollingSpeed = viewport.getWorldHeight() / 6.0f;

        Rectangle worldBoundingBox = new Rectangle(
                viewport.getScreenX(), viewport.getScreenY(), WORLD_WIDTH, WORLD_HEIGHT);

        WorldMatrix worldMatrix = new WorldMatrix(16, 16, worldBoundingBox);

        obstacleEngine = new ObstacleEngine(worldMatrix, 3.0f,
                new Texture("tree.png"),
                new Texture("rock.png"));

        playerEngine = new PlayerEngine(worldMatrix,
                new Texture("character.png"),
                new Texture("laser.png"));

        duckEngine = new DuckEngine(worldMatrix, 10.0f,
                new Texture("duck.png"));

        batch = new SpriteBatch();

        headsUpDisplay = new HeadsUpDisplay(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    //#endregion

    //#region Public Methods

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime);

        duckEngine.addDucks(deltaTime, obstacleEngine.getLastObstacleBoundingBox());
        duckEngine.renderDucks(deltaTime, backgroundScrollingSpeed, batch);

        playerEngine.renderLasers(deltaTime, backgroundScrollingSpeed, batch);
        playerEngine.renderPlayer(deltaTime, backgroundScrollingSpeed, batch);

        boolean endGame = detectPlayerAndObstacleCollision();
        if (endGame) {
            batch.end();
            gameNavigation.ShowGameOverScreen(score);

            return;
        }

        obstacleEngine.addObstacles(deltaTime);
        int skippedObstacles = obstacleEngine.renderObstacles(deltaTime, backgroundScrollingSpeed, batch);
        score += skippedObstacles;

        int ducksCaught = detectLaserAndDuckCollision();
        score += ducksCaught * 5;

        detectLaserAndObstacleCollision();

        headsUpDisplay.renderHeadsUpDisplay(score, 1, batch);

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
        gameController.register(this);
    }

    @Override
    public void dispose() {
        batch.dispose();
        headsUpDisplay.dispose();
        obstacleEngine.dispose();
        playerEngine.dispose();
        duckEngine.dispose();

        gameNavigation = null;

        if (gameController != null) {
            gameController.unregister(this);
            gameController = null;
        }
    }

    //#region IGameControls

    @Override
    public float getScreenWidth() {
        return viewport.getScreenWidth();
    }

    @Override
    public IPlayerControls getPlayerControls() {
        return playerEngine;
    }

    //#endregion

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

    public boolean detectPlayerAndObstacleCollision() {
        final IPlayer player = playerEngine.getPlayer();
        final List<IObstacle> obstacles = obstacleEngine.getObstacles();

        for (IObstacle obstacle : obstacles) {
            if (player.intersects(obstacle)) {
                return true;
            }
        }

        return false;
    }

    private int detectLaserAndDuckCollision() {
        final ListIterator<ILaser> laserIterator = playerEngine.getLasers().listIterator();

        int ducksCaught = 0;

        while (laserIterator.hasNext()) {
            ILaser laser = laserIterator.next();
            final ListIterator<IDuck> duckIterator = duckEngine.getDucks().listIterator();

            while (duckIterator.hasNext()) {
                IDuck duck = duckIterator.next();

                if (laser.intersects(duck)) {
                    laserIterator.remove();
                    duckIterator.remove();

                    ducksCaught++;

                    break;
                }
            }
        }

        return ducksCaught;
    }

    private void detectLaserAndObstacleCollision() {
        final ListIterator<ILaser> iterator = playerEngine.getLasers().listIterator();
        final List<IObstacle> obstacles = obstacleEngine.getObstacles();

        while (iterator.hasNext()) {
            ILaser laser = iterator.next();

            for (IObstacle obstacle : obstacles) {
                if (laser.intersects(obstacle)) {
                    iterator.remove();

                    break;
                }
            }
        }
    }

    //#endregion
}
