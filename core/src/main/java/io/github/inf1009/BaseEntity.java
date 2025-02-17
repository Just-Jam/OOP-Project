package io.github.inf1009;

import com.badlogic.gdx.math.Rectangle;

public abstract class BaseEntity {
    protected float x, y;
    protected float speed;
    protected Rectangle bounds;

    public BaseEntity(float x, float y, float speed, float width, float height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void move(float dx, float dy, float delta) {
        this.x += dx * speed * delta;
        this.y += dy * speed * delta;
        bounds.setPosition(x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
}
