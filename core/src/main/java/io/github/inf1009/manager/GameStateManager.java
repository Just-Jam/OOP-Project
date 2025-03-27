package io.github.inf1009.manager;

import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;

public class GameStateManager {
    public enum GameState {
        MENU,
        PAUSED,
        NORMAL,
        GAMEOVER
    }

    private GameState gameState;
    private final SoundManager soundManager;

    public GameStateManager(SoundManager soundManager) {
        this.soundManager = soundManager;
        this.gameState = GameState.NORMAL;
    }

    public void checkIllegalMove(BlockShape block, Grid grid) {
        if (grid.isOccupied(block.getGridX(), block.getGridY())) {
            triggerGameOver();
        }
    }

    public void triggerGameOver() {
        if (gameState != GameState.GAMEOVER) {
            gameState = GameState.GAMEOVER;
            soundManager.playGameOverSound();
        }
    }

    public void pauseGame() {
        if (gameState == GameState.NORMAL) {
            gameState = GameState.PAUSED;
        }
    }

    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.NORMAL;
        }
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSED;
    }

    public boolean isNormal() {
        return gameState == GameState.NORMAL;
    }
}
