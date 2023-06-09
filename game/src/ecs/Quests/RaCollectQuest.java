package ecs.Quests;

import ecs.components.InventoryComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Friendly.Hero;
import ecs.items.ItemData;
import ecs.items.newItems.Bag;
import ecs.items.newItems.BookOfRa;
import java.util.logging.Logger;
import starter.Game;

/**
 * A quest that needs the hero to collect 5 Books of Ra. When finished the quest he will level up.
 */
public class RaCollectQuest extends Quest {
    private Hero hero;
    private InventoryComponent inventoryComponent;
    private XPComponent xpComponent;
    private Logger logger = Logger.getLogger(getClass().getName());
    private int startBooks;

    private int endBooks;
    private int booksLeft;

    private boolean printedOnce;

    public RaCollectQuest() {
        super("Accepted LevelQuest. Collect 5 Books of Ra to finish Quest.");
        hero = (Hero) Game.getHero().get();
        inventoryComponent = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        xpComponent = (XPComponent) hero.getComponent(XPComponent.class).get();
        startBooks = getAmountOfBooks();
        endBooks = startBooks + 5;
        booksLeft = 5;
    }

    /** Rewards the player with a level up */
    @Override
    public void reward() {
        System.out.println("Grant Level up as reward for finishing Book Quest");
        xpComponent.addXP(xpComponent.getXPToNextLevel());
    }

    /**
     * Checks the current progress of the quest and prints if any changes to the progress have been
     * made.
     *
     * @return boolean
     */
    @Override
    public boolean checkProgress() {
        if (booksLeft != endBooks - getAmountOfBooks()) {
            System.out.println((booksLeft - 1) + " books left.");
        }
        booksLeft = endBooks - getAmountOfBooks();

        if (getAmountOfBooks() >= startBooks + 5) {
            reward();
            return true;
        }
        return false;
    }

    /**
     * Gets the current amount of books in the inventory
     *
     * @return int
     */
    private int getAmountOfBooks() {
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
        }
        return bookCounter;
    }

    /** Prints instructions to the player when the quest was accepted. */
    @Override
    public void startQuest() {
        System.out.println(
                "Started book quest. Get rewarded with 1 level up by collecting 5 Book of Ra.");
    }
}
