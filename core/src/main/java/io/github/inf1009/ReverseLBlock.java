package io.github.inf1009;

public class ReverseLBlock extends BlockShape {

    public ReverseLBlock(int gridX, int gridY, int worldWidth, int worldHeight, BlockShape.BlockType type) {
        super(gridX, gridY, worldWidth, worldHeight, type);
        this.shape = new boolean[][] {
            {false, true},
            {false, true},
            {true, true}
        };
    }
}
