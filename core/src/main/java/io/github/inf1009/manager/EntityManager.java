package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.inf1009.BlockShape;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private List<BlockShape> entityList;

    public EntityManager() {
        entityList = new ArrayList<>();
    }

    public void addBlock(BlockShape block) {
        entityList.add(block);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (BlockShape block: entityList) {
            block.draw(shapeRenderer);
        }
    }

    public void dispose() {
        entityList.clear();
    }
}
