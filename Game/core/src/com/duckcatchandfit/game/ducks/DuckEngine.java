package com.duckcatchandfit.game.ducks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.obstacles.IObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DuckEngine {

    //#region Fields

    // World dimensions
    private final WorldMatrix worldMatrix;

    // Timing
    private final float timeBetweenNewDucks;
    private float duckTimer;

    // Graphics
    private final Texture duckTexture;

    // Game objects
    private final Random random = new Random();
    private final List<IDuck> ducks = new ArrayList<>();

    //#endregion

    //#region Properties

    public List<IDuck> getDucks() { return ducks; }

    //#endregion

    //#region Initializers

    public DuckEngine(WorldMatrix worldMatrix, float timeBetweenNewDucks, Texture duckTexture) {
        this.worldMatrix = worldMatrix;
        this.timeBetweenNewDucks = timeBetweenNewDucks;
        this.duckTexture = duckTexture;
    }

    //#endregion

    //#region Public Methods

    public void addDucks(float deltaTime, List<IObstacle> obstacles) {
        duckTimer += deltaTime;

        if (duckTimer > timeBetweenNewDucks) {

            duckTimer -= timeBetweenNewDucks;
        }
    }

    public void renderDucks(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        //TODO render the ducks
    }

    public boolean detectDuckCatch() {
        //TODO detect if shoots reach the ducks
        return false;
    }

    public void dispose() {
        duckTexture.dispose();
    }

    //#endregion
}
