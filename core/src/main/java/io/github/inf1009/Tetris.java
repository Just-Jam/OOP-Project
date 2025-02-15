package io.github.inf1009;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.inf1009.screen.MainMenuScreen;

public class Tetris extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public ScreenViewport leftViewport;
    public ScreenViewport rightViewport;
    public FitViewport fitViewport;

    public Stage leftStage;
    public Stage rightStage;

    public final int gridWidth = 16;
    public final int gridHeight = 20;



    public void create() {
        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();

        //viewports
        leftViewport = new ScreenViewport();
        leftStage = new Stage(leftViewport);
        rightViewport = new ScreenViewport();
        rightStage = new Stage(rightViewport);

        fitViewport = new FitViewport(gridWidth, gridHeight);

        //font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(fitViewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
