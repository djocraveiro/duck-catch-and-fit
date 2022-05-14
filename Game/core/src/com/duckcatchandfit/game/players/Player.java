package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.obstacles.IObstacle;

public class Player implements IPlayer {

    //#region Fields

    // Position and dimensions
    private final Rectangle boundingBox;

    // Graphics
    private final Texture playerTexture;

    //#endergion

    //#region Initializers

    public Player(float x, float y, float width, float height, Texture playerTexture) {
        this.boundingBox = new Rectangle(x, y, width, height);
        this.playerTexture = playerTexture;
    }

    public Player(Rectangle boundingBox, Texture treeTexture) {
        this.boundingBox = new Rectangle(boundingBox);
        this.playerTexture = treeTexture;
    }

    //#endregion

    //#region Public Methods

    @Override
    public void update(float deltaTime) {

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

    //#endregion
}
