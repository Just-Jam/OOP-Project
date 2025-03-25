package io.github.inf1009.manager;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ViewportManager {
    private FitViewport fitViewport;
    private int gameWidth, gameHeight, previewWidth;

    public ViewportManager (int gameWidth, int gameHeight, int previewWidth) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.previewWidth = previewWidth;
        // Total width = gameWidth (game area) + previewWidth (preview area)
        fitViewport = new FitViewport(gameWidth + previewWidth, gameHeight);
    }

    public FitViewport getFitViewport() {
        return fitViewport;
    }

    public void draw() {
    	fitViewport.apply();
    }

    public void resize(int width, int height) {
    	fitViewport.update(width, height, true);
    }
}

