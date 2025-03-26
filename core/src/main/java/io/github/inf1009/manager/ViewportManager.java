package io.github.inf1009.manager;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ViewportManager {
    private FitViewport fitViewport;

    public ViewportManager (int width, int height) {
        fitViewport = new FitViewport(width, height);
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

