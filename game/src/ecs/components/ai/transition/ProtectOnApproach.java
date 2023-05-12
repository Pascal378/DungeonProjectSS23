package ecs.components.ai.transition;

import ecs.components.ai.AITools;
import ecs.entities.Entity;

/**
 * Implements an AI that protects a specific entites (e.g. Monster, Chest, WorldItem etc.) if the hero is entering
 * the given range.
 *
 * Entity will stay in fight mode
 *
 */
public class ProtectOnApproach implements ITransition{
    private final float range;

    private final Entity toProtect;

    private boolean isInFight = false;

    /**
     * Constructor needs a range and the entity to protect.
     *
     * @param range - The range in which the entity should get in fight mode
     * @param toProtect - The entity which should be protected
     */
    public ProtectOnApproach(float range, Entity toProtect){
        this.range = range;
        this.toProtect = toProtect;
    }


    /**
     * If entity isn't in fight mode yet, check if player is in range of the entity to protect
     * and set isInfight to its return value.
     *
     * @param entity associated entity
     * @return Boolean
     */
    @Override
    public boolean isInFightMode(Entity entity) {
        if(!isInFight){
            isInFight = AITools.playerInRange(toProtect, range);
        }
        return isInFight;
    }
}
