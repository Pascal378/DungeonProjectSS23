package ecs.items.newItems;

import dslToGame.AnimationBuilder;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;
import ecs.items.*;
import starter.Game;
import tools.Point;

/** The greatsword adds 20 damage to the hero if collected. */
public class Greatsword extends ItemData implements IOnCollect, IOnDrop {

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

    public Greatsword(ItemType itemType,
                            Animation inventoryTexture,
                            Animation worldTexture,
                            String itemName,
                            String description){
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        //WorldItemBuilder.buildWorldItem(this);
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

    @Override
    public void onDrop(Entity user, ItemData which, Point position) {

    }
}
