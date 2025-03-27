package io.github.inf1009.manager;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
    private final MovementManager movementManager;
    private boolean gamePaused = false;

    public InputManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void resetPauseFlag() {
        gamePaused = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                movementManager.moveLeft();
                break;
            case Input.Keys.RIGHT:
                movementManager.moveRight();
                break;
            case Input.Keys.DOWN:
                movementManager.moveDown();
                break;
            case Input.Keys.UP:
                movementManager.rotate();
                break;
            case Input.Keys.SPACE:
                movementManager.immediateDrop();
                break;
            case Input.Keys.ESCAPE:
                gamePaused = true;
                return true; // return true to signal pause handled
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // For debugging or mobile
        System.out.println("Touch at: " + screenX + ", " + screenY);
        return false;
    }
}