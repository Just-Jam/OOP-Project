package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class FireAnimation {
    private final int row;
    private final int startCol;
    private final int endCol;

    private float timeElapsed = 0f;
    private final float duration = 0.7f;

    public FireAnimation(int row, int startCol, int endCol) {
        this.row = row;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public void update(float delta) {
        timeElapsed += delta;
    }

    public boolean isFinished() {
        return timeElapsed >= duration;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        float alpha = 1.0f - (timeElapsed / duration);
        for (int col = startCol; col < endCol; col++) {
            Color baseColor = MathUtils.randomBoolean(0.5f) ? Color.ORANGE : Color.RED;
            Color fireColor = new Color(baseColor.r, baseColor.g, baseColor.b, alpha);
            shapeRenderer.setColor(fireColor);
            shapeRenderer.rect(col, row, 1, 1);
        }
    }
}
