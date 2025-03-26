package io.github.inf1009;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.SoundManager;
import io.github.inf1009.manager.ViewportManager;
import io.github.inf1009.screen.MainMenuScreen;

public class Tetris extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public SceneManager sceneManager;  // SceneManager added
    public ViewportManager viewportManager;
    public SoundManager soundManager;

    // Sets the game board size by number
    public final int GRID_COLUMNS = 10; //even number
    public final int GRID_ROWS  = 16;
    // Extra columns for next blocks
    public final int EXTRA_COLUMNS = 6;
    public final int TOTAL_COLUMNS =  GRID_COLUMNS + EXTRA_COLUMNS;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        soundManager = new SoundManager();

        // Viewport
        viewportManager = new ViewportManager(TOTAL_COLUMNS, GRID_ROWS);

        // Scale font based on viewport height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewportManager.getFitViewport().getWorldHeight() / Gdx.graphics.getHeight());

        // Initialize SceneManager
        sceneManager = new SceneManager(this);
        sceneManager.setScreen(new MainMenuScreen(this)); // Use SceneManager for screen transitions
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();

    }
}