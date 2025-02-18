package io.github.inf1009.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.inf1009.Block;
import io.github.inf1009.Grid;

public class MovementManager extends InputAdapter {
    public static void updateBlock(Block block, Grid grid, float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !block.leftCollision()) {
            block.move(-1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !block.rightCollision()) {
            block.move(1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !block.bottomCollision(grid)) {
            block.move(0, -1);
        }
    }
}
