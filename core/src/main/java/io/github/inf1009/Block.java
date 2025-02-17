package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int y) {
        gridY = y;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(gridX, gridY, 1, 1);
        shapeRenderer.end();
    }

    public void fall(Grid grid) {
        gridY -= 1;

        bounds.setPosition(gridX, gridY);
    }

    public void input(boolean[][] gridMatrix) {
        // Right movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !rightCollision(gridMatrix)) {
            gridX += 1;
        }

        // Left movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !leftCollision(gridMatrix)) {
            gridX -= 1;
        }
        // Down movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !bottomCollision(gridMatrix)) {
            gridY -= 1;
        }

//        if (gridY <= 0) {
//            gridY = worldHeight - 1;
//        }
        bounds.setPosition(gridX, gridY);
    }

    public boolean bottomCollision(boolean[][] gridMatrix) {

        if (getGridY() == 0) {
            return true;
        }
        else if (gridMatrix[getGridX()][getGridY() - 1]) {
            return true;
        }
        return false;
    }

    public boolean rightCollision(boolean[][] gridMatrix) {
        if (gridX == worldWidth - 1) {
            return true;
        }
        if (gridMatrix[gridX + 1][gridY]) {
            return true;
        }
        return false;
    }

    public boolean leftCollision(boolean[][] gridMatrix) {
        if (gridX == 0) {
            return true;
        }
        if (gridMatrix[gridX - 1][gridY]) {
            return true;
        }
        return false;
    }


}
