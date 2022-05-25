package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.duckcatchandfit.game.obstacles.IObstacle;

public interface IPlayer {

    void update(float deltaTime);

    boolean intersects(IObstacle obstacle);

    void translate(float xChange, float yChange);

    void draw(Batch batch);

    boolean canFireLaser();

    ILaser fireLaser();
}
