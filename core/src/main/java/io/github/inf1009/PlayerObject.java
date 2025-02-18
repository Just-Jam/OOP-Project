package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerObject extends BaseEntity {
    private Texture texture;
    private Sprite sprite;

    public PlayerObject(String texturePath, float x, float y, float width, float height) {
        super(x, y, 1, width, height);
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }
}

