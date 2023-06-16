package ecs.entities.Monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Friendly.Chest;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import starter.Game;

import java.util.ArrayList;
import java.util.List;

public class ChestMonster extends Monster {

    private Chest chest;
    private final String pathToIdle = "character/monster/treasurechest";
    private final String pathToRun = "character/monster/treasurechest";

    private float xSpeed = 0.1f;
    private float ySpeed = 0.1f;
    private int dmg = 3;
    private int maxHealthpoint = 4;

    public ChestMonster(int lvlFactor, Chest chest){
        super();
        this.chest = chest;
        new PositionComponent(this);
        new AIComponent(this,
            new CollideAI(5f),
            new PatrouilleWalk(2f, 4, 1000, PatrouilleWalk.MODE.BACK_AND_FORTH),
            new RangeTransition(2));
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRun);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRun);
        if (lvlFactor == 0) lvlFactor++;
        this.dmg = this.dmg * lvlFactor;
        this.maxHealthpoint = this.maxHealthpoint * lvlFactor;
        new HealthComponent(this, this.maxHealthpoint, this::onDeath, moveLeft, moveRight);
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRun);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRun);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdle);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdle);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> doDmg(other),
            null);
    }

    private void doDmg(Entity other) {
        if (!(other instanceof Hero)) return;
        if (other.getComponent(HealthComponent.class).isPresent()) {
            HealthComponent ofE = (HealthComponent) other.getComponent(HealthComponent.class).get();
            ofE.receiveHit(new Damage(this.getDmg(), DamageType.PHYSICAL, this));
        }
    }

    @Override
    public void onDeath(Entity entity) {
        spawnOriginalChest(entity);
        Game.getEntitiesToRemove().add(this);
    }

    /**
     * Generates a new chest with Items on the last position of the monster before it died.
     *
     * @param entity ChestMonster
     */
    private static void spawnOriginalChest(Entity entity) {
        // getting the last position of the entity
        PositionComponent positionComponent =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "Entity does not have a PositionComponent"));

        // generating at least three random Items.
        List<ItemData> items = new ArrayList<>();
        ItemDataGenerator generator = new ItemDataGenerator();
        for (int i = 0; i < 3; i++) {
            items.add(generator.generateItemData());
        }

        // creating a new chest with the obove generated items.
        new Chest(items, positionComponent.getPosition());
    }

    public int getDmg() {
        return dmg;
    }
}
