package ecs.items.newItems;

import dslToGame.AnimationBuilder;
import ecs.Quests.Quest;
import ecs.entities.Entity;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.items.WorldItemBuilder;
import ecs.systems.QuestSystem;
import starter.Game;

public class QuestGiver extends ItemData implements IOnUse {

    private Entity worldEntity;

    private Quest quest;

    public QuestGiver(Quest quest) {
        super(
                ItemType.Passive,
                AnimationBuilder.buildAnimation("item/world/BookOfRa"),
                AnimationBuilder.buildAnimation("item/world/BookOfRa"),
                "QuestGiver",
                "Offers Quests to the Hero");

        worldEntity = WorldItemBuilder.buildWorldItem(this);
        this.quest = quest;
        this.setOnUse(this);
    }

    public void offerQuest() {
        System.out.println("I have a quest to offer! Press E to accept the quest!");
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        QuestSystem.addQuest(quest);
    }

    public void deleteFromWorld() {
        Game.removeEntity(worldEntity);
    }

    public Entity getWorldEntity() {
        return worldEntity;
    }
}
