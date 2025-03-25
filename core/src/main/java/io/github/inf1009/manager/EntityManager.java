package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.List;

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
        currentBlock = BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows());
    }

    public BlockShape getCurrentBlock() {
        return currentBlock;
    }

    public void update(MovementManager movementManager, List<BlockShape> nextBlocks) {
        if (currentBlock.bottomCollision(grid)) {
            currentBlock.placeOnGrid(grid);
            grid.clearRow();

            // The new current block should be the first from the preview queue
            this.currentBlock = nextBlocks.remove(0);
            // Then add a new random block to keep the queue size
            nextBlocks.add(BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows()));

            movementManager.setBlock(currentBlock);
        }
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        currentBlock.draw(shapeRenderer, batch);
    }
}