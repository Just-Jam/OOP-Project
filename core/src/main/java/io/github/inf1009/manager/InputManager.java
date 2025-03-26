package io.github.inf1009.manager;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.inf1009.event.EventManager;
import io.github.inf1009.event.GameEvent;

public class InputManager extends InputAdapter {
    public MovementManager movementManager;

    public InputManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }
    public boolean gamepause=false;

    public void rotate() {
        movementManager.rotate();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case com.badlogic.gdx.Input.Keys.LEFT:
                EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.LEFT_KEY_PRESSED));
                break;
            case com.badlogic.gdx.Input.Keys.RIGHT:
                EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.RIGHT_KEY_PRESSED));
                break;
            case com.badlogic.gdx.Input.Keys.DOWN:
                EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.DOWN_KEY_PRESSED));
                break;
            case com.badlogic.gdx.Input.Keys.UP:
                EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.UP_KEY_PRESSED));
                break;
            case com.badlogic.gdx.Input.Keys.SPACE:
                EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.SPACE_KEY_PRESSED));
                break;
            case com.badlogic.gdx.Input.Keys.ESCAPE:
            	gamepause=!gamepause;
            	return gamepause;
       }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touch at: " + screenX + ", " + screenY);
        return false;  // Return false so UI elements can also receive input
    }
}

