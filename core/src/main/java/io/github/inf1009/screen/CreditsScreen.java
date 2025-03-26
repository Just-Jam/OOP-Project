package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;

public class CreditsScreen implements Screen {
    private final Tetris game;
    private final SceneManager sceneManager;
    private final Stage stage;
    private final Texture backgroundTexture;
    private final TextureButton backButton;

    public CreditsScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;

        // Use the same FitViewport as your other screens
        stage = new Stage(game.viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        // Load your background
        backgroundTexture = new Texture("CreditScreen.png");

        // Create a back button at the bottom center
        float buttonWorldWidth = 4f;   
        float buttonWorldHeight = 1f;  
        float buttonX = stage.getViewport().getWorldWidth() / 2f;
        float buttonY = 1f;
        backButton = new TextureButton("buttons/back_button.png", buttonWorldWidth, buttonWorldHeight, buttonX, buttonY, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton.getButton());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Draw the background using your FitViewport
        game.batch.setProjectionMatrix(game.viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.TOTAL_COLUMNS, game.GRID_ROWS);
        game.batch.end();

        // Draw the UI elements (label and button)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() { }
    @Override public void hide() { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
