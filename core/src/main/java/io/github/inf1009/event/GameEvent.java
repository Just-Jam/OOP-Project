package io.github.inf1009.event;

public class GameEvent {
    public enum Type {
        GAME_STARTED,
        GAME_OVER,
        GAME_PAUSED,
        GAME_RESUMED,
        GAME_TICKED, //For triggering time based game updates

        BLOCK_SPAWNED,
        BLOCK_PLACED,
        SCORE_CHECKPOINT_REACHED,

        LEFT_KEY_PRESSED,
        RIGHT_KEY_PRESSED,
        UP_KEY_PRESSED,
        DOWN_KEY_PRESSED,
        SPACE_KEY_PRESSED,
        ESC_KEY_PRESSED
    }

    public Type type;
    public Object data; // Can hold additional event data

    public GameEvent(Type type) {
        this.type = type;
    }

    public GameEvent(Type type, Object data) {
        this.type = type;
        this.data = data;
    }
}
