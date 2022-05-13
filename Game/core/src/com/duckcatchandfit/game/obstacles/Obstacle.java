package com.duckcatchandfit.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle implements IObstacle {

    //#region Fields

    // Position and dimensions
    private final Rectangle boundingBox;

    // Graphics
    private final Texture treeTexture;

    //#endregion

    //#region Initializers

    public Obstacle(float xCentre, float yCentre, float width, float height, Texture treeTexture) {
        this.boundingBox = new Rectangle(
                xCentre - width / 2,
                yCentre - height / 2,
                width, height);

        this.treeTexture = treeTexture;
    }

    //#endregion

    //#region Public Methods

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public boolean intersects(Rectangle otherBoundingBox) {
        return boundingBox.overlaps(otherBoundingBox);
    }

    @Override
    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange,
                                boundingBox.y + yChange);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(treeTexture,
                boundingBox.x, boundingBox.y,
                boundingBox.width, boundingBox.height);
    }

    @Override
    public boolean isOutside(Rectangle otherBoundingBox) {
        return !otherBoundingBox.contains(boundingBox) && !boundingBox.overlaps(otherBoundingBox);
    }

    //#endregion
}
