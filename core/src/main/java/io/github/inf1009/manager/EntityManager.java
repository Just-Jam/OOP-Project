package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009.BlockFactory;
import io.github.inf1009.BlockShape;
import io.github.inf1009.Grid;
import io.github.inf1009.event.EventManager;
import io.github.inf1009.event.GameEvent;
import io.github.inf1009.event.iEventListener;

import java.util.ArrayList;

public class EntityManager implements iEventListener {
    private BlockShape currentBlock;
    private ArrayList<BlockShape> nextBlocks;
    private final Grid grid;
    private BlockFactory blockFactory;

    public EntityManager(Grid grid) {
        this.grid = grid;
        blockFactory = new BlockFactory(grid.getColumns(), grid.getRows());
        // Initialize the next-blocks queue.
        nextBlocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            nextBlocks.add(BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows()));
        }

        EventManager.getInstance().addListener(this);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.type == GameEvent.Type.GAME_TICKED) {
            currentBlock.fall(grid);
        }
    }

    public void spawnNewBlock() {
        currentBlock = BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows());
        //Output event
        EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.BLOCK_SPAWNED, currentBlock));
    }

    public BlockShape getCurrentBlock() {
        return currentBlock;
    }

    public void update() {
        checkGameOver();
        if (currentBlock.bottomCollision(grid)) {
            currentBlock.placeOnGrid(grid);
            EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.BLOCK_PLACED));
            grid.clearRow();

            // The new current block should be the first from the preview queue
            this.currentBlock = nextBlocks.remove(0);
            EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.BLOCK_SPAWNED, currentBlock));
            // Then add a new random block to keep the queue size
            nextBlocks.add(BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows()));
        }
    }

    public void draw(ShapeRenderer shapeRenderer, Batch batch) {
        currentBlock.draw(shapeRenderer, batch);
    }

    private void checkGameOver() {
        if (grid.isOccupied(currentBlock.getGridX(), currentBlock.getGridY())) {
            EventManager.getInstance().postEvent(new GameEvent(GameEvent.Type.GAME_OVER));
        }
    }

    public ArrayList<BlockShape> getNextBlocks() {
        return nextBlocks;
    }
}
