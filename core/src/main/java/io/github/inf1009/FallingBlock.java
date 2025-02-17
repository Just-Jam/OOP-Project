package io.github.inf1009;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.inf1009.*;

public class FallingBlock extends BaseEntity {
    public FallingBlock(float x, float y, float width, float height) {
        super(x, y, 20, width, height);
    }

    public void update(float delta) {
        move(0, -1, delta); // Move down based on delta time
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(x, y, bounds.width, bounds.height);
    }

    public boolean isOutOfBounds(float worldHeight) {
        return y + bounds.height < 0;
    }
}


