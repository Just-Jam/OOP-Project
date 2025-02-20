package io.github.inf1009.manager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.inf1009.Block;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private List<Block> entityList;

    public EntityManager() {
        entityList = new ArrayList<>();
    }

    public void addBlock(Block block) {
        entityList.add(block);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (Block block: entityList) {
            block.draw(shapeRenderer);
        }
    }

    public void dispose() {
        entityList.clear();
    }

}
