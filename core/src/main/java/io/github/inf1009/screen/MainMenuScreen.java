package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;


public class MainMenuScreen implements Screen {

    final Tetris game;

    public MainMenuScreen(final Tetris game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.fitViewport.apply();
        game.batch.setProjectionMatrix(game.fitViewport.getCamera().combined);

        game.batch.begin();
        //draw text. Remember that x and y are in meters
        game.font.draw(game.batch, "Welcome to Tetris!!! ", 1, 1.5f);
        game.font.draw(game.batch, "Tap anywhere to begin!", 1, 1);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {

    }

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


