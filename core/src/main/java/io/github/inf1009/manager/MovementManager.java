package io.github.inf1009.manager;

import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;
import io.github.inf1009.event.EventManager;
import io.github.inf1009.event.GameEvent;
import io.github.inf1009.event.iEventListener;

public class MovementManager implements iEventListener {

    private BlockShape block;
    private final Grid grid;
    private final SoundManager soundManager;

    public MovementManager(Grid grid, SoundManager soundManager) {
        this.grid = grid;
        this.soundManager = soundManager;
        EventManager.getInstance().addListener(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type == GameEvent.Type.BLOCK_SPAWNED) {
            block = (BlockShape) event.data;
        }
        if (event.type == GameEvent.Type.LEFT_KEY_PRESSED) {
            moveLeft();
        }
        if (event.type == GameEvent.Type.RIGHT_KEY_PRESSED) {
            moveRight();
        }
        if (event.type == GameEvent.Type.DOWN_KEY_PRESSED) {
            moveDown();
        }
        if (event.type == GameEvent.Type.UP_KEY_PRESSED) {
            rotate();
        }
        if (event.type == GameEvent.Type.SPACE_KEY_PRESSED) {
            immediateDrop();
        }
    }

    public void moveLeft() {
        if (!block.leftCollision(grid)) {
            block.move(-1, 0);
        }
    }

    public void moveRight() {
        if (!block.rightCollision(grid)) {
            block.move(1, 0);
        }
    }

    public void moveDown() {
        if (!block.bottomCollision(grid)) {
            block.move(0, -1);
        }
    }

    public void immediateDrop() {
        // Move the block down until it collides
        while (!block.bottomCollision(grid)) {
            block.move(0, -1);
        }
    }

    public void rotate() {
        block.rotate(grid);
        soundManager.playRotateSound();
    }

    public void setBlock(BlockShape block) {
        this.block = block;
    }
}
