package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class BlockShape {
    private Texture blockImage;  // Texture to hold the block's image
    private Sprite blockSprite;  // Sprite for rendering the image

    public enum BlockType {
        RECYCLABLE,
        UNRECYCLABLE
    }

    private int gridX, gridY;
    private int worldWidth, worldHeight;
    private boolean[][] shape; // 2D matrix representing the shape
    private BlockType type;

    public BlockShape(int gridX, int gridY, int worldWidth, int worldHeight, BlockType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.type = type;

        // Load the image based on type
        if (type == BlockType.RECYCLABLE) {
            blockImage = new Texture(getRandomRecyclableImage()); // Random recyclable image
        } else {
            blockImage = new Texture(getRandomUnrecyclableImage()); // Random unrecyclable image
        }

        blockSprite = new Sprite(blockImage);
        blockSprite.setSize(1, 1);  // Set the size of the sprite to match the block size (1x1)
        blockSprite.setOrigin(0, 0);  // Ensure the origin is at the top-left corner
    }

    private String getRandomRecyclableImage() {
        String[] recyclableImages = {"bottle.png", "can.png", "cardboardbox.png", "glassbottle.png", "newspaper.png"};
        return "RECYCLABLE/" + recyclableImages[MathUtils.random(0, recyclableImages.length - 1)];
    }

    private String getRandomUnrecyclableImage() {
        String[] unrecyclableImages = {"apple.png", "banana.png", "icecream.png", "bubblewrap.png", "styrofoam.png"};
        return "UNRECYCLABLE/" + unrecyclableImages[MathUtils.random(0, unrecyclableImages.length - 1)];
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public BlockType getType() {
        return type;
    }

    public boolean[][] getShapeMatrix() {
        return shape;
    }

    protected void setShape(boolean[][] shape) {
        this.shape = shape;
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        // Loop through each cell in the shape matrix and draw the image for each 1x1 section
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col]) {
                    // For each 1x1 block that is part of the shape, draw the image
                    blockSprite.setPosition(gridX + col, gridY - row);  // Set the position for each 1x1 block
                    batch.begin();
                    blockSprite.draw(batch);  // Draw the image for the current 1x1 block
                    batch.end();
                }
            }
        }
    }

    public void drawNextBlocks(ShapeRenderer shapeRenderer, float offsetX, float offsetY) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col]) {
                    shapeRenderer.rect(offsetX + col, offsetY - row, 1, 1);
                }
            }
        }
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

    public void dispose() {
        if (blockImage != null) {
            blockImage.dispose();  // Dispose of the texture to avoid memory leaks
        }
    }
}
