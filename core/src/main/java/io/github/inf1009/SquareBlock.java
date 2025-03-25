package io.github.inf1009;

public class SquareBlock extends BlockShape {

    public SquareBlock(int gridX, int gridY, int worldWidth, int worldHeight, BlockShape.BlockType type) {
        super(gridX, gridY, worldWidth, worldHeight, type);
        setShape(new boolean[][] {
            {true, true},
            {true, true}	
        });
    }
}
