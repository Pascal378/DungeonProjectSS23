package ecs.entities.Monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.FriendlyTransition;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;

/** The Slime is an enemy monster which inherits from the Monster class. */
public class Slime extends Monster {

    private final String pathToIdleLeft = "character/monster/slime/idleLeft";
    private final String pathToIdleRight = "character/monster/slime/idleRight";
    private final String pathToRunLeft = "character/monster/slime/runLeft";
    private final String pathToRunRight = "character/monster/slime/idleRight";

    private float xSpeed = 0.05f;
    private float ySpeed = 0.05f;
    public int dmg = 1;
    private int maxHealthpoint = 8;

    /**
     * Entity with Components
     *
     * @param lvlFactor - the factor by which damage and health is increased
     */
    public Slime(int lvlFactor) {
        super();
        new PositionComponent(this);
        new AIComponent(this, new CollideAI(3f), new RadiusWalk(2, 8), new FriendlyTransition());
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        if (lvlFactor == 0) lvlFactor++;
        this.dmg = this.dmg * lvlFactor;
        this.maxHealthpoint = this.maxHealthpoint * lvlFactor;
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
        new HealthComponent(this, this.maxHealthpoint, this::onDeath, moveLeft, moveRight);
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
