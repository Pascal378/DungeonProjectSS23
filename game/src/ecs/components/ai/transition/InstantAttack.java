package ecs.components.ai.transition;

import ecs.entities.Entity;
import starter.Game;

import java.util.Optional;


public class InstantAttack implements ITransition {

    private boolean isInFight = false;
    @Override
    public boolean isInFightMode(Entity entity) {
        Optional<Entity> hero = Game.getHero();
        if (hero.isPresent()){
            return isInFight = true;
        }
        else {
            return isInFight = false;
        }

    }
}
