package com.duckcatchandfit.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.obstacles.IObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PlayerEngine {

    //#region Fields

    // World dimensions
    private final WorldMatrix worldMatrix;

    // Game objects
    private final IPlayer player;
    private int playerLastColIndex;
    private final List<ILaser> lasers = new ArrayList<>();

    //Graphics
    private final Texture playerTexture;
    private final Texture laserTexture;

    //#endregion

    //#region Properties

    public IPlayer getPlayer() { return player; }

    public List<ILaser> getLasers() { return lasers; }

    //#endregion

    //#region Initializers

    public PlayerEngine(WorldMatrix worldMatrix, Texture playerTexture, Texture laserTexture) {
        this.worldMatrix = worldMatrix;
        this.playerLastColIndex = 1;

        final int bottomRow = worldMatrix.getRowCount() - 1;
        Rectangle playerBoundingBox = worldMatrix.getCellBoundingBox(bottomRow, playerLastColIndex);

        this.playerTexture = playerTexture;
        this.laserTexture = laserTexture;
        this.player = new Player(playerBoundingBox, playerTexture, laserTexture);
    }

    //#endregion

    //#region Public Methods

    public void renderLasers(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        ListIterator<ILaser> iterator = lasers.listIterator();

        while (iterator.hasNext()) {
            ILaser laser = iterator.next();

            laser.update(deltaTime);
            laser.draw(batch);

            if (laser.isOutside(worldMatrix.getWorldBoundingBox())) {
                iterator.remove();
            }
        }
    }

    public void renderPlayer(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        player.update(deltaTime);
        player.draw(batch);
    }

    public void fireLaser() {
        if (player.canFireLaser()) {
            lasers.add(player.fireLaser());
        }
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
        laserTexture.dispose();
        lasers.clear();
    }

    //#endregion
}
