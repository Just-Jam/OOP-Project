package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;

public class CreditsScreen implements Screen {
    private static final float BUTTON_WIDTH = 4f;
    private static final float BUTTON_HEIGHT = 1f;
    private static final float BUTTON_Y = 1f;

    private final Tetris game;
    private final SceneManager sceneManager;
    private final Stage stage;

    private final Texture backgroundTexture;
    private final TextureButton backButton;

    public CreditsScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;

        stage = new Stage(game.viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("screen/CreditScreen.png");

        this.backButton = createBackButton();
        stage.addActor(backButton.getButton());
    }

    /**
     * Creates and returns the back button to return to Main Menu.
     */
    private TextureButton createBackButton() {
        float centerX = stage.getViewport().getWorldWidth() / 2f;

        return new TextureButton("buttons/back_button.png", BUTTON_WIDTH, BUTTON_HEIGHT,
                centerX, BUTTON_Y, new ClickListener() {
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

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        backButton.dispose();
    }
}
