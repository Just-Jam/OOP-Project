package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Block {
	public enum BlockType{
		RECYCLABLE, UNRECYCLABLE
	}
    private int gridX, gridY, worldWidth, worldHeight;
    private Rectangle bounds;
    private BlockType type;

    public Block(int gridX, int gridY, int worldWidth, int worldHeight, BlockType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.bounds = new Rectangle(gridX, gridY, 1, 1);
        this.type = type;
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public void setGridY(int y) { gridY = y; }
    public BlockType getType() {return type;}

    // Move Method
    public void move(int dx, int dy) {
        gridX += dx;
        gridY += dy;
        bounds.setPosition(gridX, gridY);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
     // Assign color based on block type
        if (type == BlockType.RECYCLABLE) {
            shapeRenderer.setColor(Color.GREEN);
        } else {
            shapeRenderer.setColor(Color.RED);
        }
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

    public boolean rightCollision(Grid grid) {
        if (gridX == worldWidth - 1) {
            return true;
        }
        return grid.isOccupied(gridX + 1, gridY);
    }

    public boolean leftCollision(Grid grid) {
        if (gridX == 0) {
            return true;
        }
        return grid.isOccupied(gridX - 1, gridY);
    }
}
