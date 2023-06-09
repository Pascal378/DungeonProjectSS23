package ecs.systems;

import ecs.Quests.LevelQuest;
import ecs.Quests.Quest;
import ecs.Quests.RaCollectQuest;
import ecs.components.ai.AITools;
import ecs.items.newItems.QuestGiver;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import starter.Game;

/** QuestSystem which manages current quests. */
public class QuestSystem extends ECS_System {
    private static QuestGiver questGiver;

    private static final ArrayList<Quest> questLog = new ArrayList<>();
    private int level;

    private static Logger logger = Logger.getLogger(QuestSystem.class.getName());

    private boolean inRange = false;
    private boolean offeredOnce = false;

    public QuestSystem() {
        super();
        this.level = Game.getCurrentLvl();
        questGiver = new QuestGiver(getRandomQuest());
        logger.info("Created QuestSystem.");
    }

    /**
     * Gets called each frame and checks every information regarding quests
     *
     * <p>Check if player is in range of a QuestGiver to offer quest, creates a new QuestGiver each
     * Level, checks the the progress of every quest and deletes it if completed.
     */
    @Override
    public void update() {
        // Check if player is in Range to offer quest
        if (AITools.playerInRange(questGiver.getWorldEntity(), 2f)) {
            inRange = true;
        } else {
            inRange = false;
            offeredOnce = false;
        }

        if (inRange && !offeredOnce) {
            questGiver.offerQuest();
            offeredOnce = true;
        }

        // If level changed generate new questGiver
        if (level != Game.getCurrentLvl()) {
            questGiver.deleteFromWorld();
            questGiver = new QuestGiver(getRandomQuest());
            level = Game.getCurrentLvl();
        }

        // Check progress of every quest and remove if finished
        Quest questToDelete = null;
        for (Quest quest : questLog) {
            if (quest.checkProgress()) {
                questToDelete = quest;
            }
        }

        if (questToDelete != null) {
            questLog.remove(questToDelete);
        }
    }

    /**
     * Gets random quest for the QuestGiver
     *
     * @return Quest
     */
    public Quest getRandomQuest() {
        Random random = new Random();

        if (random.nextBoolean()) {
            return new LevelQuest();
        }

        return new RaCollectQuest();
    }

    /**
     * Adds Quest to the questLog
     *
     * @param quest
     */
    public static void addQuest(Quest quest) {
        logger.info("Added Quest.\n" + quest.getQuestInfo());
        questGiver.setQuestAccepted();
        questLog.add(quest);
    }

    /**
     * Removes quest from the questLog.
     *
     * @param quest
     */
    public void removeQuest(Quest quest) {
        questLog.remove(quest);
    }
}
