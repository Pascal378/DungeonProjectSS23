package ecs.entities.Friendly;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HitboxComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.graphic.Animation;
import java.util.logging.Logger;

/**
 * A grave is spawned by a FriendlyGhost
 *
 * <p>The grave doesn't do anything special at all, it is an animated entity without any movement.
 *
 * <p>If the hero collides with the grave, the grave will call the reward function of the ghost.
 * This can only happen once in a level per grave
 */
public class Grave extends Entity {
    private final String pathToIdleRight = "dungeon/gravestone";
    private FriendlyGhost ghost;

    public boolean found = false;

    public transient Logger graveLogger = Logger.getLogger(getClass().getName());

    /**
     * Constructor
     *
     * @param ghost - Is needed for the grave to call the reward function on it
     */
    public Grave(FriendlyGhost ghost) {
        super();
        new PositionComponent(this);
        setupAnimationComponent();
        setupHitboxComponent();
        this.ghost = ghost;
    }

    public void setup() {
        new PositionComponent(this);
        setupAnimationComponent();
        setupHitboxComponent();
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        new AnimationComponent(this, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> setfound(other),
                (you, other, direction) -> graveLogger.info("graveCollisionLeave"));
    }

    /** If hero with a following ghost collides wit grave the hero gets rewarded by the ghost* */
    public void setfound(Entity other) {
        if (other instanceof Hero && !found) {
            ghost.reward();
            found = true;
        }
        graveLogger.info("FOUND");
    }
}
