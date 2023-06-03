package ecs.items.newItems;

import dslToGame.AnimationBuilder;
import ecs.Quests.Quest;
import ecs.components.IInteraction;
import ecs.components.InteractionComponent;
import ecs.entities.Entity;
import ecs.items.IOnCollect;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.items.WorldItemBuilder;
import ecs.systems.QuestSystem;
import starter.Game;

/** A QuestGiver which spawns in the level and offers Quests to the player. */
public class QuestGiver extends ItemData implements IOnCollect {

    private Entity worldEntity;

    private Quest quest;
    private boolean questAccepted = false;

    public QuestGiver(Quest quest) {
        super(
                ItemType.Passive,
                AnimationBuilder.buildAnimation("character/questgiver"),
                AnimationBuilder.buildAnimation("character/questgiver"),
                "QuestGiver",
                "Offers Quests to the Hero");

        createWorldEntity();
        this.quest = quest;
        this.setOnCollect(this);
    }

    /** Creates an Entity with which the player can interact to accept the Quest. */
    private void createWorldEntity() {
        worldEntity = WorldItemBuilder.buildWorldItem(this);
        InteractionComponent interact =
                new InteractionComponent(
                        worldEntity,
                        2f,
                        false,
                        new IInteraction() {
                            @Override
                            public void onInteraction(Entity entity) {
                                QuestSystem.addQuest(quest);
                            }
                        });
    }

    /** Prints QuestOffer to the player. */
    public void offerQuest() {
        System.out.println("I have a quest to offer! Press E to accept the quest!");
    }

    /** Deletes WorldEntity */
    public void deleteFromWorld() {
        Game.removeEntity(worldEntity);
    }

    public Entity getWorldEntity() {
        return worldEntity;
    }

    /**
     * Doesn't let the QuestGiver disappear.
     *
     * @param WorldItemEntity
     * @param whoCollides
     */
    @Override
    public void onCollect(Entity WorldItemEntity, Entity whoCollides) {
        return;
    }

    /** Sets the quest to accepted. */
    public void setQuestAccepted() {
        questAccepted = true;
    }
}
