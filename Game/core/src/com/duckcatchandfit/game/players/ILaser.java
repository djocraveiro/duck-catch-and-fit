package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.ducks.IDuck;
import com.duckcatchandfit.game.obstacles.IObstacle;

public interface ILaser {

    void update(float deltaTime);

    boolean intersects(IDuck duck);

    boolean intersects(IObstacle obstacle);

    void draw(Batch batch);

    boolean isOutside(Rectangle otherBoundingBox);
}
