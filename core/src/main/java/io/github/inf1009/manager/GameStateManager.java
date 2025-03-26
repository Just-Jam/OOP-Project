package io.github.inf1009.manager;

import java.util.List;

import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;
import io.github.inf1009.ScoreEntry;

public class GameStateManager {
    public enum GameState {
        MENU,
        PAUSED,
        NORMAL,
        GAMEOVER
    }

    private boolean scoreSaved = false;
    private Grid grid;
    private float gameSpeed; //lower = faster
    private float timer;
    private GameState gameState;
    private SoundManager soundManager;

    public GameStateManager(SoundManager soundManager) {
    	this.soundManager = soundManager;
        gameState = GameState.NORMAL;
    }


    public void checkIllegalMove(BlockShape block, Grid grid) {
    	this.grid = grid; // Store it for use in isGameOver
    	if (grid.isOccupied(block.getGridX(), block.getGridY())) {
            gameState = GameState.GAMEOVER;
            soundManager.playGameOverSound();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver(Grid grid) {
        return gameState == GameState.GAMEOVER;
    }
}
