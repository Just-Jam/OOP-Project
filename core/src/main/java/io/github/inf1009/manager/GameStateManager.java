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

    private float gameSpeed; //lower = faster
    private float timer;
    private GameState gameState;

    public GameStateManager() {
        gameState = GameState.NORMAL;
    }


    public void checkIllegalMove(BlockShape block, Grid grid) {
        if (grid.isOccupied(block.getGridX(), block.getGridY())) {
            gameState = GameState.GAMEOVER;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }
}
