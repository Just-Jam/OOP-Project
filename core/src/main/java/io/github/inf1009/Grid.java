package io.github.inf1009;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Grid {
    private final int columns;
    private final int rows;

    private boolean[][] gridMatrix;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

        gridMatrix = new boolean[rows][columns];
    }

    public boolean[][] getGridMatrix() {
        return gridMatrix;
    }

    public void drawGrid(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
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
}
