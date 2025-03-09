package io.github.inf1009;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.screen.MainMenuScreen;

public class Tetris extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public ScreenViewport leftViewport, rightViewport;
    public FitViewport fitViewport;
    public Stage leftStage, rightStage;
    public SceneManager sceneManager;  // SceneManager added

    // Sets the game board size by number
    public final int GRID_COLUMNS = 10; //even number
    public final int GRID_ROWS  = 15;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Viewports
        leftViewport = new ScreenViewport();
        leftStage = new Stage(leftViewport);

        rightViewport = new ScreenViewport();
        rightStage = new Stage(rightViewport);

        fitViewport = new FitViewport(GRID_COLUMNS, GRID_ROWS);

        // Scale font based on viewport height
        font.setUseIntegerPositions(false);
        font.getData().setScale(fitViewport.getWorldHeight() / Gdx.graphics.getHeight());

        // Initialize SceneManager
        sceneManager = new SceneManager(this);
        sceneManager.setScreen(new MainMenuScreen(this)); // Use SceneManager for screen transitions
    }

    public void render() {

        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();

    }
}
