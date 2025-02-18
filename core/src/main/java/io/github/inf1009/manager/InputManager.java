package io.github.inf1009.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
    public MovementManager movementManager;

    public InputManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case com.badlogic.gdx.Input.Keys.LEFT:
                movementManager.moveLeft();
                break;
            case com.badlogic.gdx.Input.Keys.RIGHT:
                movementManager.moveRight();
                break;
            case com.badlogic.gdx.Input.Keys.DOWN:
                movementManager.moveDown();
                break;
        }
        return true;
    }
}
