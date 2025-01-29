package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerEntity {
    private float x;
    private float y;
    private float speed;
    private Texture tex;
    private float fallSpeed = 1;

    private float movement_left_bound;
    private float movement_right_bound;

    public PlayerEntity (String texturePath, float x_, float y_, float speed_, float centre_X){
        tex = new Texture(Gdx.files.internal(texturePath));
        x = x_;
        y = y_;
        speed = speed_;
        movement_left_bound = centre_X - 200;
        movement_right_bound = centre_X + 200;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    void setX(float x_) {
        x = x_;
    }

    void setY(float y_) {
        y = y_;
    }

    void setSpeed(float speed_) {
        speed = speed_;
    }

    void drawBatch(SpriteBatch batch) {
        batch.draw(tex, getX(), getY(), tex.getWidth(), tex.getHeight());
    }

    void movement() {
        if (getX() > movement_left_bound) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ) setX(getX() - getSpeed() * Gdx.graphics.getDeltaTime());
        }
        if (getX() < movement_right_bound) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ) setX(getX() + getSpeed() * Gdx.graphics.getDeltaTime());
        }
    }

    void fall() {
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= fallSpeed * 2;
        }
        y -= fallSpeed;
        if (y <= 0) {
            setY(600);
        }
    }
}
