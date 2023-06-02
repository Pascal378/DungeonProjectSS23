package ecs.Quests;

import ecs.components.HealthComponent;
import ecs.entities.Friendly.Hero;
import java.util.logging.Logger;
import starter.Game;

public class LevelQuest extends Quest {

    private Hero hero;
    private HealthComponent healthComponent;

    private int startLevel;
    private int endLevel;
    private Logger logger = Logger.getLogger(getClass().getName());

    public LevelQuest() {
        hero = (Hero) Game.getHero().get();
        healthComponent =
                (HealthComponent) hero.getComponent(HealthComponent.class.getClass()).get();
        startLevel = Game.getCurrentLvl();
        endLevel = startLevel + 5;
    }

    @Override
    public void reward() {
        healthComponent.setMaximalHealthpoints(healthComponent.getMaximalHealthpoints() + 10);
        logger.info("Rewarded due to finished quest. Increased maximum health of the hero by 10.");
    }

    @Override
    public boolean checkProgress() {
        if (startLevel >= endLevel) {
            reward();
            return true;
        }
        ;
        return false;
    }

    @Override
    public void startQuest() {
        logger.info("Quest started at " + startLevel + ". Completed by " + (startLevel + 5) + ".");
    }
}
