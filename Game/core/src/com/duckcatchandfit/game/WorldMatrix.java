package com.duckcatchandfit.game;

import com.badlogic.gdx.math.Rectangle;

public class WorldMatrix {

    //#region Fields

    // World dimensions
    private final Rectangle worldBoundingBox;

    // Matrix
    private final float cellWidth;
    private final float cellHeight;
    private final int rowCount;
    private final int colCount;
    private final Rectangle[][] cells;

    //#endregion

    //#region Properties

    public Rectangle getWorldBoundingBox() { return worldBoundingBox; }

    public float getCellWidth() { return cellWidth; }

    public float getCellHeight() { return cellHeight; }

    public int getRowCount() { return rowCount; }

    public int getColumCount() { return colCount; }

    //#endregion

    //#region Initializers

    public WorldMatrix(float cellWidth, float cellHeight, Rectangle worldBoundingBox) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.worldBoundingBox = new Rectangle(worldBoundingBox);

        rowCount = (int)Math.floor(this.worldBoundingBox.height / this.cellHeight) + 1;
        colCount = (int)Math.floor(this.worldBoundingBox.width / this.cellWidth);

        float remainWidth = this.worldBoundingBox.width % this.cellWidth;
        cells = getMatrixCells(remainWidth / 2.0f);
    }

    //#endregion

    //#region Public Methods

    public Rectangle getCellBoundingBox(int rowIndex, int colIndex) {
        if (rowIndex < 0 || rowIndex >= rowCount) {
            throw new IndexOutOfBoundsException("rowIndex");
        }

        if (colIndex < 0 || colIndex >= colCount) {
            throw new IndexOutOfBoundsException("colIndex");
        }

        return cells[rowIndex][colIndex];
    }

    //#endregion

    //#region Private Methods

    private Rectangle[][] getMatrixCells(float xOffset) {
        final Rectangle[][] cells = new Rectangle[rowCount][];

        // from top left of the screen
        float cellY = worldBoundingBox.height;
        float cellX = xOffset;

        for (int rowIndex = 0; rowIndex < cells.length; rowIndex++) {
            cells[rowIndex] = new Rectangle[colCount];

            for (int colIndex = 0; colIndex < cells[rowIndex].length; colIndex++) {
                cells[rowIndex][colIndex] = new Rectangle(cellX, cellY, cellWidth, cellHeight);

                cellX += cellWidth;
            }

            cellX = xOffset;
            cellY -= cellHeight;
        }

        return cells;
    }

    //#endregion
}
