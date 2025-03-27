package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.InputManager;
import io.github.inf1009.manager.SceneManager;

public class PauseScreen implements Screen {
    private static final float BUTTON_WIDTH = 4f;
    private static final float BUTTON_HEIGHT = 1f;
    private static final float BACK_Y = 4f;
    private static final float QUIT_Y = 3f;

    private final Tetris game;
    private final SceneManager sceneManager;
    private final Stage stage;

    private final Texture backgroundTexture;
    private final TextureButton backButton;
    private final TextureButton quitButton;

    private final Screen previousScreen;
    private final InputManager inputManager;

    public PauseScreen(final Tetris game, Screen previousScreen, InputManager inputManager) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.inputManager = inputManager;
        this.sceneManager = game.sceneManager;

        this.stage = new Stage(game.viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        this.backgroundTexture = new Texture("screen/game_pause.png");

        this.backButton = createBackButton();
        this.quitButton = createQuitButton();

        stage.addActor(backButton.getButton());
        stage.addActor(quitButton.getButton());
    }

    private TextureButton createBackButton() {
        float centerX = game.TOTAL_COLUMNS / 2f;

        return new TextureButton("buttons/back_button.png", BUTTON_WIDTH, BUTTON_HEIGHT, centerX, BACK_Y,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        sceneManager.popScreen();           // Resume game
                        inputManager.resetPauseFlag();      // Reset pause state
                    }
                });
    }

    private TextureButton createQuitButton() {
        float centerX = game.TOTAL_COLUMNS / 2f;

        return new TextureButton("buttons/quit_button.png", BUTTON_WIDTH, BUTTON_HEIGHT, centerX, QUIT_Y,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        sceneManager.setScreen(new MainMenuScreen(game));
                    }
                });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.batch.setProjectionMatrix(game.viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.TOTAL_COLUMNS, game.GRID_ROWS);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        sceneManager.backgroundMusic.pause();
    }

    @Override public void hide() {}
    @Override public void pause() {}

    @Override
    public void resume() {
        sceneManager.backgroundMusic.play();
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        backButton.dispose();
        quitButton.dispose();
    }
}
