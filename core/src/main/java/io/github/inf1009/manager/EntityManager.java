package io.github.inf1009.manager;

import com.badlogic.gdx.math.Rectangle;

public class EntityManager {
    protected float x, y;
    protected float speed;
    protected Rectangle bounds;

    public EntityManager(float x, float y, float speed, float width, float height) {
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
