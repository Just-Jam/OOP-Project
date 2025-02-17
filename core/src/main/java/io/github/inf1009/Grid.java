package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Grid {
    private final int columns;
    private final int rows;

    private boolean[][] gridMatrix;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

        gridMatrix = new boolean[columns][rows];

    }

    public boolean[][] getGridMatrix() {
        return gridMatrix;
    }

    public void addBlock(int x, int y) {
        gridMatrix[x][y] = true;
    }

    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        drawGrid(shapeRenderer, fitViewport);
        drawBlocks(shapeRenderer);
    }

    private void drawGrid(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        //draw grid
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
        // Begin drawing
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Set the color for the grid lines
        shapeRenderer.setColor(1, 1, 1, 1); // White color
        // Draw vertical lines
        for (int x = 0; x <= columns; x++) {
            shapeRenderer.rect(x - 0.02f, 0, 0.04f, rows); // Thin rectangle for vertical line
        }
        // Draw horizontal lines as rectangles
        for (int y = 0; y <= rows; y++) {
            shapeRenderer.rect(0, y - 0.02f, columns, 0.04f); // Thin rectangle for horizontal line
        }
        // End drawing
        shapeRenderer.end();
    }

    private void drawBlocks(ShapeRenderer shapeRenderer) {
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (gridMatrix[col][row]) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.YELLOW);
                    shapeRenderer.rect(col, row, 1, 1);
                    shapeRenderer.end();
                }
            }
        }
    }
}
