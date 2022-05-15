package com.duckcatchandfit.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.players.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class ObstacleEngine {

    //#region Fields

    // World dimensions
    private final WorldMatrix worldMatrix;

    // Timing
    private final float timeBetweenNewObstacles;
    private float obstacleTimer;

    // Graphics
    private final Texture treeTexture;
    private final Texture rockTexture;

    // Game objects
    private int lastObstacleColIndex;
    private Rectangle lastObstacleBoundingBox;
    private final Random random = new Random();
    private final List<IObstacle> obstacles = new ArrayList<>();

    //#endregion

    //#region Initializers

    public ObstacleEngine(WorldMatrix worldMatrix, float timeBetweenNewObstacles, Texture treeTexture,
              Texture rockTexture) {
        this.worldMatrix = worldMatrix;
        this.timeBetweenNewObstacles = timeBetweenNewObstacles;
        this.obstacleTimer = timeBetweenNewObstacles;
        this.treeTexture = treeTexture;
        this.rockTexture = rockTexture;

        lastObstacleColIndex = -1;
    }

    //#endregion

    //#region Public Methods

    public void addObstacles(float deltaTime) {
        obstacleTimer += deltaTime;

        if (obstacleTimer > timeBetweenNewObstacles) {
            lastObstacleColIndex = getObstacleColIndex();
            Rectangle boundingBox = worldMatrix.getCellBoundingBox(0, lastObstacleColIndex);

            if (lastObstacleBoundingBox == null || !lastObstacleBoundingBox.overlaps(boundingBox)) {
                obstacles.add(new Obstacle(boundingBox, getObstacleTexture()));

                lastObstacleBoundingBox = worldMatrix.getCellBoundingBox(1, lastObstacleColIndex);
            }

            obstacleTimer -= timeBetweenNewObstacles;
        }
    }

    public int renderObstacles(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        ListIterator<IObstacle> iterator = obstacles.listIterator();
        int removedCount = 0;

        while (iterator.hasNext()) {
            IObstacle obstacle = iterator.next();

            obstacle.update(deltaTime);
            obstacle.translate(0, deltaTime * -scrollingSpeed);
            obstacle.draw(batch);

            if (obstacle.isOutside(worldMatrix.getWorldBoundingBox())) {
                iterator.remove();
                removedCount++;
            }
        }

        return removedCount;
    }

    public boolean detectCollisions(IPlayer player) {
        for (IObstacle obstacle : obstacles) {
            if (player.intersects(obstacle)) {
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        treeTexture.dispose();
        rockTexture.dispose();
        obstacles.clear();
    }

    //#endregion

    //#region Private Methods

    private int getObstacleColIndex() {
        return getRandomNumber(0, worldMatrix.getColumCount() - 1);
    }

    private Texture getObstacleTexture() {
        int number = getRandomNumber(0, 10);

        if (number >= 3) {
            return treeTexture;
        }
        else {
            return rockTexture;
        }
    }

    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    //#endregion
}
