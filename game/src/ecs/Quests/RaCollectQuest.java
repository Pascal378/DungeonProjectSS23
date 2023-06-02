package ecs.Quests;

import ecs.components.InventoryComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Friendly.Hero;
import ecs.items.ItemData;
import ecs.items.newItems.Bag;
import ecs.items.newItems.BookOfRa;
import java.util.logging.Logger;
import starter.Game;

public class RaCollectQuest extends Quest {
    private Hero hero;
    private InventoryComponent inventoryComponent;
    private XPComponent xpComponent;
    private Logger logger = Logger.getLogger(getClass().getName());

    public RaCollectQuest() {
        hero = (Hero) Game.getHero().get();
        inventoryComponent =
                (InventoryComponent) hero.getComponent(InventoryComponent.class.getClass()).get();
        xpComponent = (XPComponent) hero.getComponent(XPComponent.class.getClass()).get();
    }

    @Override
    public void reward() {
        logger.info("Grant Level up as reward for finishing Book Quest");
        xpComponent.addXP(xpComponent.getXPToNextLevel());
    }

    @Override
    public boolean checkProgress() {
        int bookCounter = 0;
        for (ItemData item : inventoryComponent.getItems()) {
            if (item instanceof BookOfRa) {
                bookCounter++;
            }

            if (item instanceof Bag) {
                for (ItemData book : ((Bag) item).getItems()) {
                    bookCounter++;
                }
            }

            if (bookCounter >= 5) {
                reward();
                return true;
            }
        }
        return false;
    }

    @Override
    public void startQuest() {
        logger.info("Started book quest. Get rewarded with 1 level up by collecting 5 Book of Ra.");
    }
}
