package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.ViewportManager;

public class GameOverScreen implements Screen {
    private final Tetris game;
    private Texture backgroundTexture;
    private Texture newGameButtonTexture;
    private TextureButton newGameButton;
    private TextureButton quitButton;
    private final int worldWidth;
    private final int worldHeight;

    private SceneManager sceneManager;
    private ViewportManager viewportManager;
    private Stage stage;

    public GameOverScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        viewportManager = game.viewportManager;

        backgroundTexture = new Texture("game_over.png");
        stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        worldWidth = game.GRID_COLUMNS;
        worldHeight = game.GRID_ROWS;

//         Create button using reusable TextureButton class
        newGameButton = new TextureButton("buttons/new_game_button.png", 4, 1, worldWidth / 2, 6, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new GameScreen(game));
                sceneManager.backgroundMusic.play();
                sceneManager.menuMusic.stop();
            }
        });

        quitButton = new TextureButton("buttons/quit_button.png", 4, 1, worldWidth / 2, 5, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(newGameButton.getButton());
        stage.addActor(quitButton.getButton());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

       viewportManager.draw();

        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.menuMusic.play();
        }
    }

    @Override
    public void show() {}

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
        stage.dispose();
        newGameButton.dispose();
        backgroundTexture.dispose();
    }
}
