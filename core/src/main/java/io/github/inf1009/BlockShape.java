package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class BlockShape {
	public enum BlockType {
        RECYCLABLE,
        UNRECYCLABLE
    }
    protected int gridX, gridY;
    protected int worldWidth, worldHeight;
    protected boolean[][] shape; // 2D matrix representing the shape
    protected BlockType type;

    public BlockShape(int gridX, int gridY, int worldWidth, int worldHeight, BlockType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.type = type;
    }

    public boolean[][] getShapeMatrix() {
        return shape;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(type == BlockType.RECYCLABLE ? Color.GREEN : Color.RED);

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col]) {
                    shapeRenderer.rect(gridX + col, gridY - row, 1, 1);
                }
            }
        }

        shapeRenderer.end();
    }
    
    public void rotate(Grid grid) {
        boolean[][] rotated = rotateBlockClockwise(shape);

        // Check if the rotated version would be out of bounds or collide
        if (canFit(rotated, grid)) {
            this.shape = rotated;
        }
    }

    private boolean[][] rotateBlockClockwise(boolean[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] rotated = new boolean[cols][rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[col][rows - 1 - row] = matrix[row][col];
            }
        }

        return rotated;
    }

    private boolean canFit(boolean[][] newShape, Grid grid) {
        for (int row = 0; row < newShape.length; row++) {
            for (int col = 0; col < newShape[0].length; col++) {
                if (newShape[row][col]) {
                    int x = gridX + col;
                    int y = gridY - row;

                    if (x < 0 || x >= worldWidth || y < 0 || y >= worldHeight || grid.isOccupied(x, y)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void move(int dx, int dy) {
        gridX += dx;
        gridY += dy;
    }

    public boolean bottomCollision(Grid grid) {
        return checkCollision(grid, 0, -1);
    }

    public boolean leftCollision(Grid grid) {
        return checkCollision(grid, -1, 0);
    }

    public boolean rightCollision(Grid grid) {
        return checkCollision(grid, 1, 0);
    }

    public void fall(Grid grid) {
        if (!bottomCollision(grid)) {
            move(0, -1);
        }
    }

    public void placeOnGrid(Grid grid) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col]) {
                    int x = gridX + col;
                    int y = gridY - row;
                    if (x >= 0 && x < worldWidth && y >= 0 && y < worldHeight) {
                        grid.addBlock(x, y, type);
                    }
                }
            }
        }
    }

    private boolean checkCollision(Grid grid, int dx, int dy) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col]) {
                    int newX = gridX + col + dx;
                    int newY = gridY - row + dy;

                    // Bounds check
                    if (newX < 0 || newX >= worldWidth || newY < 0 || newY >= worldHeight) {
                        return true;
                    }

                    if (grid.isOccupied(newX, newY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getGridX() {
    	return gridX; 
    }
    
    public int getGridY() {
    	return gridY;
    }
    
    public BlockType getType() {
    	return type;
    }
}
