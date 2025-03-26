package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.ViewportManager;

public class MainMenuScreen implements Screen {

    private final Tetris game;
    private final SceneManager sceneManager;
    private final ViewportManager viewportManager;
    private Texture backgroundTexture;
    private Stage stage;

    private int worldWidth, worldHeight;
    private TextureButton playButton;
    private TextureButton creditButton;
    private TextureButton howToPlayButton;
    private String player_name;

    public MainMenuScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.viewportManager = game.viewportManager;

        backgroundTexture = new Texture("title_art.png");
        stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        worldWidth = game.GRID_COLUMNS;
        worldHeight = game.GRID_ROWS;

        // Play button
        playButton = new TextureButton("buttons/play_button.png", 4, 1, worldWidth / 2f, 7f, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new GameScreen(game, player_name));
                sceneManager.backgroundMusic.play();
                sceneManager.menuMusic.stop();
            }
        });
        stage.addActor(playButton.getButton());

        // Credit button
        creditButton = new TextureButton("buttons/credits_button.png", 4, 1, worldWidth / 2f, 5.5f, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new CreditsScreen(game));
            }
        });
        stage.addActor(creditButton.getButton());

        // How to Play button
        howToPlayButton = new TextureButton("buttons/howtoplay_button.png", 4, 1, worldWidth / 2f, 4f, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new InstructionScreen(game));
            }
        });
        stage.addActor(howToPlayButton.getButton());
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

        // Optional keyboard control for music
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            sceneManager.menuMusic.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            sceneManager.menuMusic.stop();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {
        viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
