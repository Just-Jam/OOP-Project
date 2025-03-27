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
import io.github.inf1009.manager.ViewportManager;

public class InstructionScreen implements Screen {
    private static final float BUTTON_WIDTH = 4f;
    private static final float BUTTON_HEIGHT = 1f;
    private static final float BUTTON_Y = 1.5f;

    private final Tetris game;
    private final SceneManager sceneManager;
    private final ViewportManager viewportManager;

    private final Stage stage;
    private final Texture backgroundTexture;
    private final TextureButton homepageButton;

    private final int worldWidth;
    private final int worldHeight;

    public InstructionScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.viewportManager = game.viewportManager;

        this.worldWidth = game.GRID_COLUMNS;
        this.worldHeight = game.GRID_ROWS;

        this.stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        this.backgroundTexture = new Texture("screen/InstructionsScreen.jpg");
        this.homepageButton = createHomepageButton();
        stage.addActor(homepageButton.getButton());
    }

    private TextureButton createHomepageButton() {
        float centerX = worldWidth / 2f;

        return new TextureButton("buttons/homepage_button.png", BUTTON_WIDTH, BUTTON_HEIGHT,
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

        viewportManager.draw();

        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
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
        homepageButton.dispose();
    }
}
