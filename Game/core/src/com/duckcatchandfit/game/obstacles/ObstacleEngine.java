package com.duckcatchandfit.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ObstacleEngine {

    //#region Fields

    // World dimensions
    private final Rectangle worldBoundingBox;

    // Timing
    private final float timeBetweenNewObstacles;
    private float obstacleTimer = 0;

    // Graphics
    private final Texture treeTexture;

    // Game objects
    private final List<IObstacle> obstacles = new ArrayList<>();

    //#endregion

    //#region Initializers

    public ObstacleEngine(Rectangle worldBoundingBox, float timeBetweenNewObstacles) {
        this.worldBoundingBox = new Rectangle(worldBoundingBox);
        this.timeBetweenNewObstacles = timeBetweenNewObstacles;

        treeTexture = new Texture("tree.png");
    }

    //#endregion

    //#region Public Methods

    public void addObstacles(float deltaTime) {
        obstacleTimer += deltaTime;

        if (obstacleTimer > timeBetweenNewObstacles) {
            obstacles.add(new Obstacle(
                    worldBoundingBox.width / 2.0f,
                    worldBoundingBox.height,
                    16, 16, treeTexture));

            obstacleTimer -= timeBetweenNewObstacles;
        }
    }

    public void renderObstacles(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        ListIterator<IObstacle> iterator = obstacles.listIterator();

        while (iterator.hasNext()) {
            IObstacle obstacle = iterator.next();

            obstacle.update(deltaTime);
            obstacle.translate(0, deltaTime * -scrollingSpeed);
            obstacle.draw(batch);

            if (obstacle.isOutside(worldBoundingBox)) {
                iterator.remove();
            }
        }
    }

    //#endregion
}
