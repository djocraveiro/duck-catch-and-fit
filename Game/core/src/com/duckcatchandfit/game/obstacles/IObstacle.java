package com.duckcatchandfit.game.obstacles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface IObstacle {

    void update(float deltaTime);

    boolean intersects(Rectangle otherRectangle);

    void translate(float xChange, float yChange);

    void draw(Batch batch);

    boolean isOutside(Rectangle otherBoundingBox);
}
