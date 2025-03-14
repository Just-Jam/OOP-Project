package io.github.inf1009.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;

public class GameOverScreen implements Screen {
    private final Tetris game;

    public GameOverScreen(final Tetris game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.fitViewport.apply();
        game.batch.setProjectionMatrix(game.fitViewport.getCamera().combined);

        game.batch.begin();
        // Draw text (x and y are in meters)
        game.font.draw(game.batch, "Game Over", 1.3f, 5.5f);

        game.batch.end();

    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        game.fitViewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
