package ecs.systems;

import ecs.Quests.LevelQuest;
import ecs.Quests.Quest;
import ecs.Quests.RaCollectQuest;
import ecs.components.ai.AITools;
import ecs.items.newItems.QuestGiver;
import java.util.ArrayList;
import java.util.Random;
import starter.Game;

public class QuestSystem extends ECS_System {
    private QuestGiver questGiver;

    private static final ArrayList<Quest> questLog = new ArrayList<>();
    private int level;

    public QuestSystem() {
        super();
        this.level = Game.getCurrentLvl();
        this.questGiver = new QuestGiver(getRandomQuest());
    }

    @Override
    public void update() {
        // Check if player is in Range to offer quest
        if (AITools.playerInRange(questGiver.getWorldEntity(), 2f)) {
            questGiver.offerQuest();
        }

        // If level changed generate new questGiver
        if (level != Game.getCurrentLvl()) {
            questGiver.deleteFromWorld();
            questGiver = new QuestGiver(getRandomQuest());
        }

        // Check progress of every quest and remove if finished
        for (Quest quest : questLog) {
            if (quest.checkProgress()) {
                removeQuest(quest);
            }
        }
    }

    public Quest getRandomQuest() {
        Random random = new Random();

        if (random.nextBoolean()) {
            return new LevelQuest();
        }

        return new RaCollectQuest();
    }

    public static void addQuest(Quest quest) {
        questLog.add(quest);
    }

    public void removeQuest(Quest quest) {
        questLog.remove(quest);
    }
}
