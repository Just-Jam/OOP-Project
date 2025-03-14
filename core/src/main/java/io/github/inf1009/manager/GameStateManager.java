package io.github.inf1009.manager;

import io.github.inf1009.Block;
import io.github.inf1009.Grid;

public class GameStateManager {
    public enum GameState {
        MENU,
        PAUSED,
        NORMAL,
        GAMEOVER
    }

    private float gameSpeed;
    private GameState gameState = GameState.NORMAL;

    public void checkIllegalMove(Block block, Grid grid) {
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
