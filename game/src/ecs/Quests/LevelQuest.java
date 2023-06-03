package ecs.Quests;

import ecs.components.HealthComponent;
import ecs.entities.Friendly.Hero;
import java.util.logging.Logger;
import starter.Game;

/** A Quest that rewards the player with 10 bonus max HP if he reaches 5 more levels. */
public class LevelQuest extends Quest {

    private Hero hero;
    private HealthComponent healthComponent;

    private int currentLevel;
    private int endLevel;

    private boolean printedOnce = false;
    private Logger logger = Logger.getLogger(getClass().getName());

    public LevelQuest() {
        super("Accepted LevelQuest. Reach next 5 Levels to finish Quest.");
        hero = (Hero) Game.getHero().get();
        healthComponent = (HealthComponent) hero.getComponent(HealthComponent.class).get();
        currentLevel = Game.getCurrentLvl();
        endLevel = currentLevel + 5;
    }

    /** Rewards the player with 10 bonus maximum HP */
    @Override
    public void reward() {
        healthComponent.setMaximalHealthpoints(healthComponent.getMaximalHealthpoints() + 10);
        System.out.println(
                "Rewarded due to finished quest. Increased maximum health of the hero by 10.");
    }

    /**
     * Checks progress of the current quest and prints out levels left to the player.
     *
     * @return boolean
     */
    @Override
    public boolean checkProgress() {
        if (currentLevel != Game.getCurrentLvl()) {
            currentLevel = Game.getCurrentLvl();
            printedOnce = false;
        }
        if (currentLevel >= endLevel) {
            reward();
            return true;
        }
        if (!printedOnce) {
            System.out.println(endLevel - currentLevel + " Levels missing.");
            printedOnce = true;
        }
        return false;
    }

    /** Prints information about the quest to complete. */
    @Override
    public void startQuest() {
        logger.info(
                "Quest started at " + currentLevel + ". Completed by " + (currentLevel + 5) + ".");
    }
}
