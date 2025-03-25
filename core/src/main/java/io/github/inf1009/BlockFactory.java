package io.github.inf1009;

import com.badlogic.gdx.math.MathUtils;

public class BlockFactory {
    public static BlockShape createRandomBlock(int worldWidth, int worldHeight) {
        BlockShape.BlockType type = MathUtils.randomBoolean() ? BlockShape.BlockType.RECYCLABLE : BlockShape.BlockType.UNRECYCLABLE;

        int spawnX = (worldWidth / 2) - 1;
        int spawnY = worldHeight - 1;

        int randomShape = MathUtils.random(0, 3);

        switch (randomShape) {
            case 0:
                return new SquareBlock(spawnX, spawnY, worldWidth, worldHeight, type);
            case 1:
                return new LineBlock(spawnX, spawnY, worldWidth, worldHeight, type);
            case 2:
                return new LBlock(spawnX, spawnY, worldWidth, worldHeight, type);
            case 3:
                return new ReverseLBlock(spawnX, spawnY, worldWidth, worldHeight, type);
            default:
            	return new SquareBlock(spawnX, spawnY, worldWidth, worldHeight, type);
        }
    }
}
