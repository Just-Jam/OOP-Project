package io.github.inf1009;

import io.github.inf1009.manager.EntityManager;

public abstract class BaseEntity extends EntityManager {
    public BaseEntity(float x, float y, float speed, float width, float height) {
        super(x, y, speed, width, height);
    }
}
