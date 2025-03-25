package io.github.inf1009;

public class LBlock extends BlockShape {

    public LBlock(int gridX, int gridY, int worldWidth, int worldHeight, BlockShape.BlockType type) {
        super(gridX, gridY, worldWidth, worldHeight, type);
        this.shape = new boolean[][] {
            {true, false},
            {true, false},
            {true, true}
        };
    }
}
