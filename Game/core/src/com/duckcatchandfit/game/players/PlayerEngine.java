package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.WorldMatrix;

public class PlayerEngine {

    //#region Fields

    // World dimensions
    private final WorldMatrix worldMatrix;

    // Game objects
    private final IPlayer player;
    private int playerLastRowIndex, playerLastColIndex;

    //Graphics
    private final Texture playerTexture;

    //#endregion

    //#region Initializers

    public PlayerEngine(WorldMatrix worldMatrix, Texture playerTexture) {
        this.worldMatrix = worldMatrix;
        this.playerLastRowIndex = worldMatrix.getRowCount() - 1;
        this.playerLastColIndex = 1;

        Rectangle playerBoundingBox = worldMatrix.getCellBoundingBox(playerLastRowIndex, playerLastColIndex);
        this.playerTexture = playerTexture;
        this.player = new Player(playerBoundingBox, playerTexture);
    }

    //#endregion

    //#region Public Methods

    public void renderPlayer(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        player.update(deltaTime);
        player.draw(batch);
    }

    public void movePlayerLeft() {
        if (playerLastColIndex == 0) {
            return;
        }

        playerLastColIndex--;
        player.translate(-worldMatrix.getCellWidth(), 0);
    }

    public void movePlayerRight() {
        if (playerLastColIndex == worldMatrix.getColumCount() - 1) {
            return;
        }

        playerLastColIndex++;
        player.translate(worldMatrix.getCellWidth(), 0);
    }

    public void dispose() {
        playerTexture.dispose();
    }

    //#endregion
}
