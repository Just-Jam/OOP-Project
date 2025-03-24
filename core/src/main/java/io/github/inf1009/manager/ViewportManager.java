package io.github.inf1009.manager;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ViewportManager {
    private ScreenViewport leftViewport, rightViewport;
    private FitViewport fitViewport;
    private Stage leftStage, rightStage;

    public ViewportManager (int width, int height) {
        leftViewport = new ScreenViewport();
        leftStage = new Stage(leftViewport);

        rightViewport = new ScreenViewport();
        rightStage = new Stage(rightViewport);

        fitViewport = new FitViewport(width, height);
    }

    public FitViewport getFitViewport() {
        return fitViewport;
    }

    public void draw() {
        leftStage.act();
        rightStage.act();

        leftViewport.apply();
        leftStage.draw();

        rightViewport.apply();
        rightStage.draw();

        fitViewport.apply();
    }

    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
        leftViewport.update(fitViewport.getLeftGutterWidth(), height, true);
        leftViewport.setScreenPosition(0, 0);
        rightViewport.update(fitViewport.getRightGutterWidth(), height, true);
        rightViewport.setScreenPosition(fitViewport.getRightGutterX(), 0);
    }
}
