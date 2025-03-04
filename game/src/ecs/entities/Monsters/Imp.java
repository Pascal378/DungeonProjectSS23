package ecs.entities.Monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.heroLastPosition;
import ecs.components.ai.transition.FriendlyTransition;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;

/** The Imp is an enemy monster which inherits from the Monster class. */
public class Imp extends Monster {

    private final String pathToIdleLeft = "character/monster/imp/idleLeft";
    private final String pathToIdleRight = "character/monster/imp/idleRight";
    private final String pathToRunLeft = "character/monster/imp/runLeft";
    private final String pathToRunRight = "character/monster/imp/idleRight";

    private float xSpeed = 0.2f;
    private float ySpeed = 0.2f;
    private int dmg = 1;
    private int maxHealthpoint = 5;

    /**
     * Entity with Components
     *
     * @param lvlFactor - the factor by which damage and health is increased
     */
    public Imp(int lvlFactor) {
        super();
        new PositionComponent(this);
        new AIComponent(this, new CollideAI(4f), new heroLastPosition(5), new FriendlyTransition());
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        if (lvlFactor == 0) lvlFactor++;
        this.dmg = this.dmg * lvlFactor;
        this.maxHealthpoint = this.maxHealthpoint * lvlFactor;
        new HealthComponent(this, this.maxHealthpoint, this::onDeath, idleLeft, idleRight);
    }

    public Imp() {
        super();
        new PositionComponent(this);
        new AIComponent(this, new CollideAI(4f), new heroLastPosition(5), new FriendlyTransition());
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        this.dmg = this.dmg;
        this.maxHealthpoint = this.maxHealthpoint;
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> doDmg(other),
                (you, other, direction) -> System.out.println(""));
    }

    private void doDmg(Entity other) {
        if (!(other instanceof Hero)) return;
        if (other.getComponent(HealthComponent.class).isPresent()) {
            HealthComponent ofE = (HealthComponent) other.getComponent(HealthComponent.class).get();
            ofE.receiveHit(new Damage(this.getDmg(), DamageType.PHYSICAL, this));
        }
    }

    public int getDmg() {
        return dmg;
    }
}
