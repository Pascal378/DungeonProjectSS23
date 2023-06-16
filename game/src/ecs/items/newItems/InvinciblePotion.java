package ecs.items.newItems;

import dslToGame.AnimationBuilder;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;
import ecs.items.*;

import java.util.Timer;
import java.util.TimerTask;
import starter.Game;
import tools.Point;

/** The invincible Potion is an item that makes the hero immortal onCollect. */
public class InvinciblePotion extends ItemData implements IOnCollect, IOnDrop {

    public InvinciblePotion() {
        super(
                ItemType.Active,
                AnimationBuilder.buildAnimation("item/world/InvinciblePotion"),
                AnimationBuilder.buildAnimation("item/world/InvinciblePotion"),
                "Invincible Potion",
                "A Potion which makes you immortal for 5 seconds");

        WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
    }

    public InvinciblePotion(ItemType itemType,
                    Animation inventoryTexture,
                    Animation worldTexture,
                    String itemName,
                    String description){
        super(itemType, inventoryTexture, worldTexture, itemName, description);
        //WorldItemBuilder.buildWorldItem(this);
        this.setOnCollect(this);
    }

    /** Resets invincible to false */
    public void resetInvincible(Entity e) {
        HealthComponent innerHCP = (HealthComponent) e.getComponent(HealthComponent.class).get();
        innerHCP.setInvincible(false);
        itemLogger.info("Not longer invincible");
    }

    /**
     * Sets the hero invincible to true
     *
     * @param WorldItemEntity
     * @param whoCollides
     */
    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {

        if (whoCollides instanceof Hero) {
            Game.removeEntity(WorldItemEntity);
            HealthComponent hCp = null;

            // Set invincible if HealthComponent is present
            if (whoCollides.getComponent(HealthComponent.class).isPresent()) {
                hCp = (HealthComponent) whoCollides.getComponent(HealthComponent.class).get();
            }

            if (hCp != null) {
                hCp.setInvincible(true);
                itemLogger.info("Invincible for 5 seconds");

                // After 5 seconds, set invincible false
                Timer timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                resetInvincible(whoCollides);
                            }
                        },
                        (long) 5 * 1000);
            }
        }
    }

    @Override
    public void onDrop(Entity user, ItemData which, Point position) {

    }
}
