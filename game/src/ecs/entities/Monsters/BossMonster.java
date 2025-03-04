package ecs.entities.Monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.BossAI;
import ecs.components.ai.idle.BossWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.skill.FireballSkill;
import ecs.components.skill.IceballSkill;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.graphic.Animation;
import java.util.logging.Logger;

public class BossMonster extends Monster {

    private final String pathToIdleLeft = "DK/idleLeft";
    private final String pathToIdleRight = "DK/idleRight";
    private final String pathToRunLeft = "DK/runLeft";
    private final String pathToRunRight = "DK/runRight";
    private final String onHit = "DK/runRight";
    private final int fireballCoolDown = 1;
    private float xSpeed = 0.09f;
    private float ySpeed = 0.09f;
    private int dmg = 7;
    private transient HealthComponent hp;

    private transient PositionComponent position;
    private transient Hero hero;
    private transient Skill firstSkill;
    private transient Skill secondSkill;
    private transient SkillComponent sCp;
    private transient VelocityComponent vC;
    private float attackrange = 1.5f;

    private transient Logger bossMonsterLogger = Logger.getLogger(getClass().getName());

    /**
     * Constructor
     *
     * @param hero
     */
    public BossMonster(Hero hero) {
        super();
        setupVelocityComponent();
        setupSkillComponent();
        setupIceballSkill();
        setupFireballSkill();
        setupHealthComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        this.hero = hero;
        this.position = new PositionComponent(this);
        new AIComponent(
                this,
                new BossAI(attackrange, firstSkill, secondSkill, hp, vC),
                new BossWalk(2, 1f, hp),
                new RangeTransition(attackrange + 0.5f));
    }

    private void setupHealthComponent() {
        Animation hit = AnimationBuilder.buildAnimation(onHit);
        this.hp = new HealthComponent(this, 90, this::onDeath, hit, hit);
    }

    /**
     * onDeath function which execute if the given Entity has no HP left
     *
     * @param entity on Death of the given entity
     */
    public void onDeath(Entity entity) {
        bossMonsterLogger.info("Boss Monster dead");
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        this.vC = new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> bossMonsterLogger.info("collide"),
                (you, other, direction) -> bossMonsterLogger.info("collide"));
    }

    private void setupSkillComponent() {
        sCp = new SkillComponent(this);
    }

    private void setupIceballSkill() {
        sCp.addSkill(
                firstSkill =
                        new Skill(
                                new IceballSkill(() -> hero.getPosition().getPoint()),
                                fireballCoolDown));
    }

    private void setupFireballSkill() {
        sCp.addSkill(
                secondSkill =
                        new Skill(
                                new FireballSkill(() -> hero.getPosition().getPoint()),
                                fireballCoolDown));
    }

    public Hero getHero() {
        return hero;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getDmg() {
        return dmg;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public void setPosition(PositionComponent position) {
        this.position = position;
    }
}
