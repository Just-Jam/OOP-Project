package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009.BlockFactory;
import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;

public class EntityManager {
    private BlockShape currentBlock;
    private Grid grid;
    private BlockFactory blockFactory;

    public EntityManager(Grid grid) {
        this.grid = grid;
        blockFactory = new BlockFactory(grid.getColumns(), grid.getRows());
    }

    public void spawnNewBlock() {
        currentBlock = blockFactory.createRandomBlock();
    }

    public BlockShape getCurrentBlock() {
        return currentBlock;
    }

    public void update(MovementManager movementManager) {
        if (currentBlock.bottomCollision(grid)) {
            currentBlock.placeOnGrid(grid); // places all sub-blocks into grid
            grid.clearRow();

            // Spawn a new block with a fresh random type
            spawnNewBlock();

            movementManager.setBlock(currentBlock);
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        currentBlock.draw(shapeRenderer);
    }
}
