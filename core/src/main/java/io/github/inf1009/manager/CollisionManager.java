package io.github.inf1009.manager;

import com.badlogic.gdx.utils.Array;
import io.github.inf1009.FallingBlock;
import io.github.inf1009.PlayerObject;

public class CollisionManager {
    public static void checkCollisions(PlayerObject player, Array<FallingBlock> fallingBlocks) {
        for (int i = fallingBlocks.size - 1; i >= 0; i--) {
            FallingBlock block = fallingBlocks.get(i);
            if (player.getBounds().overlaps(block.getBounds())) { // FIXED
                fallingBlocks.removeIndex(i);
            }
        }
    }
}

