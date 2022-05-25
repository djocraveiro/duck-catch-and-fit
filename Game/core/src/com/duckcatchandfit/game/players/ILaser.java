package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.ducks.IDuck;
import com.duckcatchandfit.game.obstacles.IObstacle;

public interface ILaser {

    boolean intersects(IDuck duck);

    boolean intersects(IObstacle obstacle);

    void update(float deltaTime);

    void draw(Batch batch);

    boolean isOutside(Rectangle otherBoundingBox);
}
