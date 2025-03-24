package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.ViewportManager;

public class MainMenuScreen implements Screen {

    private final Tetris game;
    private final SceneManager sceneManager;
    private Texture backgroundTexture;
    private Stage stage;

    private int worldWidth, worldHeight;
    private TextureButton playButton;

    private ViewportManager viewportManager;

    public MainMenuScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager; // Use SceneManager from Tetris
        backgroundTexture = new Texture("title_art.png");

        viewportManager = game.viewportManager;
        stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        worldWidth = game.GRID_COLUMNS;
        worldHeight = game.GRID_ROWS;

//         Create button using reusable TextureButton class
        playButton = new TextureButton("buttons/play_button.png", 5, 2, worldWidth / 2, worldHeight / 2, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new GameScreen(game));
                sceneManager.backgroundMusic.play();
                sceneManager.menuMusic.stop();
            }
        });

        stage.addActor(playButton.getButton());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewportManager.draw();
        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.GRID_COLUMNS, game.GRID_ROWS);
        // Draw text (x and y are in meters)
//        game.font.draw(game.batch, "Welcome to Our Engine!!! ", 1.3f, 5.5f);
//        game.font.draw(game.batch, "Tap anywhere to begin!", 1.3f, 5);
//        game.font.draw(game.batch, "INSTRUCTIONS:\nClick < to shift left"
//        		+ "\nClick > to shift right\nClick ESC to return to Main Menu"
//        		+ "\nClick Space at any time to pause\nor resume the game!", 1.3f, 4.5f);


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

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
        viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
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
