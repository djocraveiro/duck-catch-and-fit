package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.ducks.IDuck;
import com.duckcatchandfit.game.obstacles.IObstacle;

public class Laser implements ILaser {

    //#region Fields

    // Position and dimensions
    private final Rectangle boundingBox;

    // Laser characteristics
    private final float movementSpeed; // world units per second

    // Graphics
    private final Texture laserTexture;

    //#endergion

    //#region Initializers

    public Laser(Rectangle boundingBox, float movementSpeed, Texture laserTexture) {
        this.boundingBox = boundingBox;
        this.movementSpeed = movementSpeed;
        this.laserTexture = laserTexture;
    }

    //#endregion

    //#region Public Methods

    @Override
    public boolean intersects(IDuck duck) {
        return duck.intersects(boundingBox);
    }

    @Override
    public boolean intersects(IObstacle obstacle) {
        return obstacle.intersects(boundingBox);
    }

    @Override
    public void update(float deltaTime) {
        final float yChange = deltaTime * movementSpeed;

        boundingBox.setPosition(boundingBox.x,
                boundingBox.y + yChange);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(laserTexture,
                boundingBox.x, boundingBox.y,
                boundingBox.width, boundingBox.height);
    }

    @Override
    public boolean isOutside(Rectangle otherBoundingBox) {
        return !otherBoundingBox.contains(boundingBox) && !boundingBox.overlaps(otherBoundingBox);
    }

    //#endregion
}
