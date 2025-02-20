package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;
import io.github.inf1009.manager.SceneManager;

public class MainMenuScreen implements Screen {

    private final Tetris game;
    private final SceneManager sceneManager;

    public MainMenuScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager; // Use SceneManager from Tetris
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.fitViewport.apply();
        game.batch.setProjectionMatrix(game.fitViewport.getCamera().combined);

        game.batch.begin();
        // Draw text (x and y are in meters)
        game.font.draw(game.batch, "Welcome to Our Engine!!! ", 1.3f, 5.5f);
        game.font.draw(game.batch, "Tap anywhere to begin!", 1.3f, 5);
        game.font.draw(game.batch, "INSTRUCTIONS:\nClick < to shift left"
        		+ "\nClick > to shift right\nClick ESC to return to Main Menu", 1.3f, 4.5f);
        
        game.batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            sceneManager.menuMusic.play();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            sceneManager.menuMusic.stop();
        }
        // Change screen using SceneManager when tapped
        else if (Gdx.input.isTouched()) {
            sceneManager.setScreen(new GameScreen(game));
            sceneManager.backgroundMusic.play();
            sceneManager.menuMusic.stop();
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
