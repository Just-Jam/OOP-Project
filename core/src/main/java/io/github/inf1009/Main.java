package io.github.inf1009;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import sun.tools.jconsole.JConsole;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private PlayerEntity player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new PlayerEntity("droplet.png", 640, 600, 200, 640);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        float centre_X = Gdx.graphics.getWidth() / 2.0f;
        float centre_Y = Gdx.graphics.getHeight() / 2.0f;


        batch.begin();
        player.drawBatch(batch);
        batch.end();


        // Toggle fullscreen with F
        if (Gdx.graphics.isFullscreen()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F))
                Gdx.graphics.setWindowedMode(1280, 720);
        }
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F))
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        player.movement();
        player.fall();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
