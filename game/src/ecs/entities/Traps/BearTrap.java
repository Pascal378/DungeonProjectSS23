package ecs.entities.Traps;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.graphic.Animation;
import java.util.ArrayList;

public class BearTrap extends Trap {
    private final String idle = "character/monster/beartrap";

    ArrayList<Entity> inRange = new ArrayList<>();

    public BearTrap() {
        super(false, false, 2);
        setup();
    }

    public void setup() {
        new PositionComponent(this);
        setupAnimationComponent();
        setupHitboxComponent();
        this.setTrapDmg(1);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(idle);
        new AnimationComponent(this, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> triggerAction(other),
                (you, other, direction) -> System.out.println());
    }

    void triggerAction(Entity other) {
        this.inRange.add(other);
        if (!this.isTriggered()) doDmg(other);
        if (other.getComponent(HealthComponent.class).isPresent()) this.setTriggered(true);
    }

    /**
     * Applies damage to entity
     *
     * @param other
     */
    public void doDmg(Entity other) {
        if (other.getComponent(HealthComponent.class).isPresent()) {
            HealthComponent ofE = (HealthComponent) other.getComponent(HealthComponent.class).get();
            ofE.receiveHit(new Damage(this.getTrapDmg(), DamageType.PHYSICAL, this));
        }
    }
}
