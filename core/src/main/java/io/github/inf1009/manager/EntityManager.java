package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.List;

import io.github.inf1009.BlockFactory;
import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;

public class EntityManager {
    private BlockShape currentBlock;
    private final Grid grid;

    public EntityManager(Grid grid) {
        this.grid = grid;
    }

    public void spawnNewBlock(BlockShape nextBlock) {
        this.currentBlock = nextBlock;
    }

    public BlockShape getCurrentBlock() {
        return currentBlock;
    }

    public void update(MovementManager movementManager, List<BlockShape> nextBlocks) {
        if (currentBlock.bottomCollision(grid)) {
            currentBlock.placeOnGrid(grid);
            grid.clearRow();

            if (!nextBlocks.isEmpty()) {
                BlockShape newBlock = nextBlocks.remove(0);
                spawnNewBlock(newBlock);
                nextBlocks.add(BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows()));
                movementManager.setBlock(currentBlock);
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        if (currentBlock != null) {
            currentBlock.draw(shapeRenderer, batch);
        }
    }
}
