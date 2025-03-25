package io.github.inf1009.manager;

import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;

public class MovementManager {

    private BlockShape block;
    private Grid grid;
    private SoundManager soundManager;

    public MovementManager(BlockShape block, Grid grid, SoundManager soundManager) {
        this.block = block;
        this.grid = grid;
        this.soundManager = soundManager;
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
