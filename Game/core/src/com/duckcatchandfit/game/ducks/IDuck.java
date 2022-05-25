package com.duckcatchandfit.game.ducks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface IDuck {

    boolean intersects(Rectangle otherBoundingBox);

    void translate(float xChange, float yChange);

    void draw(Batch batch);

    boolean isOutside(Rectangle otherBoundingBox);
}
