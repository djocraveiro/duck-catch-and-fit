package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.obstacles.IObstacle;

public class Player implements IPlayer {

    //#region Fields

    // Position and dimensions
    private final Rectangle boundingBox;

    // Timing
    private final float timeBetweenShots = 1.0f;
    private float timeSinceLastShot = 0;

    // Graphics
    private final Texture playerTexture;
    private final Texture laserTexture;

    //#endergion

    //#region Initializers

    public Player(Rectangle boundingBox, Texture playerTexture, Texture laserTexture) {
        this.boundingBox = boundingBox;
        this.playerTexture = playerTexture;
        this.laserTexture = laserTexture;
    }

    //#endregion

    //#region Public Methods

    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    @Override
    public boolean intersects(IObstacle obstacle) {
        return obstacle.intersects(boundingBox);
    }

    @Override
    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange,
                boundingBox.y + yChange);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(playerTexture,
                boundingBox.x, boundingBox.y,
                boundingBox.width, boundingBox.height);
    }

    @Override
    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    @Override
    public ILaser fireLaser() {
        Rectangle laserBoundingBox = new Rectangle(
                boundingBox.x + boundingBox.width * 0.7f,
                boundingBox.y + boundingBox.height * 0.9f,
                1.0f, 4.0f);

        ILaser laser = new Laser(laserBoundingBox, 60, laserTexture);

        timeSinceLastShot = 0;

        return laser;
    }

    //#endregion
}
