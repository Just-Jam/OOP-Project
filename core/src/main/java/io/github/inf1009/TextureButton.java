package io.github.inf1009;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class TextureButton {
    private final ImageButton button;
    private final Texture texture;

    public TextureButton(String texturePath, float width, float height, float x, float y, ClickListener listener) {
        // Load texture
        texture = new Texture(texturePath);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        // Create ImageButton
        button = new ImageButton(drawable);
        button.setSize(width , height);
        // Set the position of the button
        button.setPosition(x - (width / 2), y - (height / 2));
        button.getImage().setAlign(Align.center);  // Centers the image within its cell
        button.setDebug(true);  // Draws the hitbox in yellow TO REMOVE
        // Add click listener
        button.addListener(listener);

    }

    public ImageButton getButton() {
        return button;
    }

    public void dispose() {
        texture.dispose();
    }
}
