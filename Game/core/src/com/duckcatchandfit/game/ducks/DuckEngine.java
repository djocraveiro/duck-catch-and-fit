package com.duckcatchandfit.game.ducks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.duckcatchandfit.game.WorldMatrix;
import com.duckcatchandfit.game.obstacles.IObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
    private Rectangle lastDuckBoundingBox;
    private final Random random = new Random();
    private final List<IDuck> ducks = new ArrayList<>();

    //#endregion

    //#region Properties

    public List<IDuck> getDucks() { return ducks; }

    public Rectangle getLastDuckBoundingBox() {
        if (lastDuckBoundingBox != null) {
            return lastDuckBoundingBox;
        }

        return null;
    }

    //#endregion

    //#region Initializers

    public DuckEngine(WorldMatrix worldMatrix, float timeBetweenNewDucks, Texture duckTexture) {
        this.worldMatrix = worldMatrix;
        this.timeBetweenNewDucks = timeBetweenNewDucks;
        this.duckTexture = duckTexture;
    }

    //#endregion

    //#region Public Methods

    public void addDucks(float deltaTime, Rectangle lockedBoundingBox) {
        duckTimer += deltaTime;

        if (duckTimer > timeBetweenNewDucks) {
            final int duckColIndex = getDuckColIndex();
            Rectangle boundingBox = worldMatrix.getCellBoundingBox(0, duckColIndex);

            final boolean canAdd = (lastDuckBoundingBox == null || !lastDuckBoundingBox.overlaps(boundingBox))
                    && (lockedBoundingBox == null || !lockedBoundingBox.overlaps(boundingBox));

            if (canAdd) {
                Rectangle duckBoundingBox = new Rectangle(boundingBox);
                ducks.add(new Duck(duckBoundingBox, duckTexture));

                lastDuckBoundingBox = new Rectangle(duckBoundingBox);

                duckTimer -= timeBetweenNewDucks;
            }
        }
    }

    public void renderDucks(float deltaTime, float scrollingSpeed, SpriteBatch batch) {
        ListIterator<IDuck> iterator = ducks.listIterator();

        final float yChange = deltaTime * -scrollingSpeed;

        while (iterator.hasNext()) {
            IDuck duck = iterator.next();

            duck.translate(0, yChange);
            duck.draw(batch);

            if (duck.isOutside(worldMatrix.getWorldBoundingBox())) {
                iterator.remove();
            }
        }
    }

    public void dispose() {
        duckTexture.dispose();
        ducks.clear();
    }

    //#endregion

    //#region Private Methods

    private int getDuckColIndex() {
        return getRandomNumber(0, worldMatrix.getColumCount() - 1);
    }

    public int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    //#endregion
}
