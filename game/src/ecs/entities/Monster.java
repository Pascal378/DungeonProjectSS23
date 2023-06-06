package ecs.entities;

import ecs.components.IOnDeathFunction;
import ecs.components.PositionComponent;

/**
 * The monster is an entity. It is defined as an abstract class because we never have a Monster
 * object. All future existing monsters will inherit from this class
 */
public class Monster extends Entity implements IOnDeathFunction {

    private Hero hero;

    /**
     * Entity with Components
     */
    public Monster() {
        super();

        new PositionComponent(this);
    }

    @Override
    public void onDeath(Entity entity) {

    }
}
