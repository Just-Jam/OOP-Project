package io.github.inf1009;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class FallingBlock {
    private float x, y;
    private float width, height;
    private float fallSpeed; // Speed of falling block
    private Rectangle bounds;

    public FallingBlock(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
        this.fallSpeed = 20;
    }

    public void update(float delta) {
        y -= fallSpeed * delta; // Move block down at a speed of 20 units per second
        bounds.setPosition(x, y);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(x, y, width, height);
    }

    public boolean isOutOfBounds(float worldHeight) {
        return y + height < 0; // Check if the block has fallen below the screen
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

