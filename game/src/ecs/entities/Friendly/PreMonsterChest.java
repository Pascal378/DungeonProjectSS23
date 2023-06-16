package ecs.entities.Friendly;

import ecs.components.AnimationComponent;
import ecs.components.InteractionComponent;
import ecs.components.InventoryComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Monsters.ChestMonster;
import ecs.graphic.Animation;
import ecs.items.ItemData;
import starter.Game;
import tools.Point;

import java.util.List;

public class PreMonsterChest extends Chest {

    /**
     * Creates a new Chest which drops the given items on interaction
     *
     * @param itemData which the chest is supposed to drop
     * @param position the position where the chest is placed
     */
    public PreMonsterChest(List<ItemData> itemData, Point position) {
        super();
        new PositionComponent(this, position);
        InventoryComponent ic = new InventoryComponent(this, itemData.size());
        itemData.forEach(ic::addItem);
        new InteractionComponent(this, defaultInteractionRadius, false,this::spawnChestMonster );
        AnimationComponent ac =
            new AnimationComponent(
                this,
                new Animation(DEFAULT_CLOSED_ANIMATION_FRAMES, 50, false),
                new Animation(DEFAULT_OPENING_ANIMATION_FRAMES, 50, false));
    }

    /**
     * spawn a monster by interacting with the chest
     * @param entity - chest
     */
    public void spawnChestMonster(Entity entity) {
        new ChestMonster(Game.getCurrentLvl(), (Chest)entity);
        Game.removeEntity(entity);
    }
}
