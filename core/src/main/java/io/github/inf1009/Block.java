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

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(gridX, gridY, 1, 1);
        shapeRenderer.end();
    }

    public void update() {
        gridY -= 1;
        bounds.setPosition(gridX, gridY);

        if (gridY <= 0) {
            gridY = worldHeight - 1;
        }
    }

    public void input() {
        // Right movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && gridX < worldWidth - 1) {
            gridX += 1;
        }

        // Left movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && gridX > 0) {
            gridX -= 1;
        }
        // Down movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && gridY > 0) {
            gridY -= 1;
        }

        if (gridY <= 0) {
            gridY = worldHeight - 1;
        }
        bounds.setPosition(gridX, gridY);
    }
}
