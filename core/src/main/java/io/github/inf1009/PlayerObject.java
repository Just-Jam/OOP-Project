package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PlayerObject {
    private int width, height, gridX, gridY, worldWidth, worldHeight;
    private Texture texture;
    private Sprite sprite;
    private Rectangle rectangle;

    public PlayerObject(String texturePath, int width, int height, int gridX, int gridY, int worldWidth, int worldHeight) {
        //Width and height in game world units or square units
        this.width = width;
        this.height = height;
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.texture = new Texture(Gdx.files.internal(texturePath));

        this.sprite = new Sprite(texture);
        sprite.setSize(width, height);

        rectangle = new Rectangle();
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Texture getTexture() {
        return texture;
    }

    public void draw(SpriteBatch batch, float worldWidth) {
        //draw
        sprite.setPosition(gridX, gridY);
        sprite.draw(batch);
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

        // Up movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && gridY < worldHeight - 1) {
            gridY += 1;
        }

        // Down movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && gridY > 0) {
            gridY -= 1;
        }
    }


    public void dispose() {
        texture.dispose();
    }

}
