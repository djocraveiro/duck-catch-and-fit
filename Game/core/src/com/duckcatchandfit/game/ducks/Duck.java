package com.duckcatchandfit.game.ducks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Duck implements IDuck {

    //#region Fields

    // Position and dimensions
    private final Rectangle boundingBox;

    // Graphics
    private final Texture duckTexture;

    //#endregion

    //#region Initializers

    public Duck(Rectangle boundingBox, Texture duckTexture) {
        this.boundingBox = boundingBox;
        this.duckTexture = duckTexture;
    }

    //#endregion

    //#region Public Methods

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
        batch.draw(duckTexture,
                boundingBox.x, boundingBox.y,
                boundingBox.width, boundingBox.height);
    }

    @Override
    public boolean isOutside(Rectangle otherBoundingBox) {
        return !otherBoundingBox.contains(boundingBox) && !boundingBox.overlaps(otherBoundingBox);
    }

    //#endregion
}
