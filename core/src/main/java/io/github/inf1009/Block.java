package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    private int gridX, gridY, worldWidth, worldHeight;
    private Rectangle bounds;

    public Block(int gridX, int gridY, int worldWidth, int worldHeight) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.bounds = new Rectangle(gridX, gridY, 1, 1);
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public void setGridY(int y) { gridY = y; }

    // Move Method
    public void move(int dx, int dy) {
        gridX += dx;
        gridY += dy;
        bounds.setPosition(gridX, gridY);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(gridX, gridY, 1, 1);
        shapeRenderer.end();
    }

    public void fall(Grid grid) {
    	gridY = bottomCollision(grid) ? worldHeight - 1 : gridY - 1;
        bounds.setPosition(gridX, gridY);
    }

    public boolean bottomCollision(Grid grid) {
    	return gridY == 0 || grid.isOccupied(gridX, gridY - 1);
    }

    public boolean rightCollision() {
        return gridX == worldWidth - 1;  // Prevents moving past right boundary
    }

    public boolean leftCollision() {
        return gridX == 0;  // Prevents moving past left boundary
    }
}
