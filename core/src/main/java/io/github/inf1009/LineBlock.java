package io.github.inf1009;

public class LineBlock extends BlockShape {

    public LineBlock(int gridX, int gridY, int worldWidth, int worldHeight, BlockShape.BlockType type) {
        super(gridX, gridY, worldWidth, worldHeight, type);
        this.shape = new boolean[][] {
            {true, true}
        };
    }
}
