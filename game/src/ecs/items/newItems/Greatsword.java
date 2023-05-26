package ecs.items.newItems;

import dslToGame.AnimationBuilder;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.IOnCollect;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.items.WorldItemBuilder;
import starter.Game;

/** The greatsword adds 20 damage to the hero if collected. */
public class Greatsword extends ItemData implements IOnCollect {

    private final int dmg = 20;

    public Greatsword() {
        super(
                ItemType.Passive,
                AnimationBuilder.buildAnimation("item/world/Greatsword"),
                AnimationBuilder.buildAnimation("item/world/Greatsword"),
                "Greatsword",
                "Increases the owners damage by 20");

        WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
    }

    /**
     * Sets the current damage of the hero plus 20
     *
     * @param WorldItemEntity
     * @param whoCollides
     */
    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        if (whoCollides instanceof Hero) {
            Game.removeEntity(WorldItemEntity);
            Hero hero = (Hero) whoCollides;
            int currentDmg = hero.getDmg();
            hero.setDmg(currentDmg + dmg);
            itemLogger.info("New damage: " + hero.getDmg());
        }
    }
}
