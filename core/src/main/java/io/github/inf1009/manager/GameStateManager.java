package io.github.inf1009.manager;

import io.github.inf1009.event.EventManager;
import io.github.inf1009.event.GameEvent;
import io.github.inf1009.event.iEventListener;

public class GameStateManager implements iEventListener {
    private float gameSpeed = 0.4f; //lower = faster
    private float timer = 0;

    public GameStateManager() {
        EventManager.getInstance().addListener(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type == GameEvent.Type.SCORE_CHECKPOINT_REACHED) {
            if ((int) event.data > 3000) {
                gameSpeed = 0.15f;
            }
            if ((int) event.data > 2000) {
                gameSpeed = 0.20f;
            }
            if ((int) event.data > 1000) {
                gameSpeed = 0.25f;
            }
        }
    }

    public void update(float delta) {
        if (timer >= gameSpeed) {
            EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.GAME_TICKED));
            timer = 0;
        }
        timer += delta;
    }
}
