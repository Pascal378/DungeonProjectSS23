package ecs.entities.Friendly;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.skill.*;
import ecs.components.xp.ILevelUp;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import ecs.graphic.Animation;
import java.io.File;
import java.util.logging.Logger;
import starter.Game;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */
public class Hero extends Entity implements IOnDeathFunction, ILevelUp {

    private final int fireballCoolDown = 0;
    private final int healSkillCoolDown = 1;

    private final int enrageCoolDown = 30;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;
    private int dmg = 10;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String onHit = "knight/hit";

    private transient SkillComponent sCp;
    private transient HealthComponent hp;

    private transient PositionComponent position;

    private transient Skill firstSkill;
    private transient Skill secondSkill;
    private transient Skill thirdSkill;

    private transient PlayableComponent pc;
    private transient XPComponent xpCmp;

    private transient InventoryComponent inv;

    int currentHealth;

    private transient Logger heroLogger = Logger.getLogger(getClass().getName());

    private static boolean dead = false;

    /** Entity with Components */
    public Hero() {
        super();
        this.position = new PositionComponent(this);
        inv = new InventoryComponent(this, 12);
        setupVelocityComponent();
        setupSkillComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        this.pc = new PlayableComponent(this);
        setupFireballSkill();
        setupHealthComponent();
        setupXpComponent();
        pc.setSkillSlot1(firstSkill);
        this.hp.setCurrentHealthpoints(55);
        currentHealth = this.hp.getCurrentHealthpoints();
    }

    public void setup() {
        new PositionComponent(this);
        inv = new InventoryComponent(this, 12);
        setupVelocityComponent();
        setupSkillComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        this.pc = new PlayableComponent(this);
        setupFireballSkill();
        setupHealthComponent();
        setupXpComponent();
        pc.setSkillSlot1(firstSkill);
        this.hp.setCurrentHealthpoints(55);
        currentHealth = this.hp.getCurrentHealthpoints();
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupSkillComponent() {
        sCp = new SkillComponent(this);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupFireballSkill() {
        firstSkill =
                new Skill(
                        new FireballSkill(SkillTools::getCursorPositionAsPoint), fireballCoolDown);
    }

    private void setupHealSkill() {
        secondSkill = new Skill(new HealSkill(), healSkillCoolDown);
        sCp.addSkill(secondSkill);
    }

    private void setupEnrageSkill() {
        thirdSkill = new Skill(new EnrageSkill(), enrageCoolDown);
        sCp.addSkill(thirdSkill);
    }

    /** Modifies the current health by passed amount */
    public void setHealth(int amount) {
        heroLogger.info("HP before: " + this.hp.getCurrentHealthpoints());
        this.hp.setCurrentHealthpoints(this.hp.getCurrentHealthpoints() + amount);
        heroLogger.info("HP after: " + this.hp.getCurrentHealthpoints());
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> heroLogger.info("collide"),
                (you, other, direction) -> heroLogger.info("collide"));
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getDmg() {
        return dmg;
    }

    // TODO: Fix death animation
    private void setupHealthComponent() {
        Animation hit = AnimationBuilder.buildAnimation(onHit);
        this.hp = new HealthComponent(this, 100, this::onDeath, hit, hit);
    }

    private void setupXpComponent() {
        xpCmp = new XPComponent(this, this);
    }

    public XPComponent getXpCmp() {
        return xpCmp;
    }

    public void setXpCmp(XPComponent xpCmp) {
        this.xpCmp = xpCmp;
    }

    @Override
    /**
     * onDeath function which execute if the given Entity has no HP left
     *
     * @param entity on Death of the given entity
     */
    public void onDeath(Entity entity) {
        File saveGame = new File("SaveFile.ser");
        if (saveGame.exists()) {
            saveGame.delete();
        }
        Game.getGameOverMenu().showMenu();
    }

    /**
     * LevelUp function that currently adds Skills if needed level is reached.
     *
     * @param nexLevel is the new level of the entity
     */
    @Override
    public void onLevelUp(long nexLevel) {
        heroLogger.info("You leveled up to Level " + nexLevel);
        if (nexLevel == 1) {
            setupHealSkill();
            pc.setSkillSlot2(secondSkill);
        }
        if (nexLevel == 2) {
            setupEnrageSkill();
            pc.setSkillSlot3(thirdSkill);
        }
    }

    public InventoryComponent getInv() {
        return inv;
    }

    public void setInv(InventoryComponent inv) {
        this.inv = inv;
    }

    public int getCurrentHealth() {
        return hp.getCurrentHealthpoints();
    }

    public int getMaxHealth() {
        return hp.getMaximalHealthpoints();
    }

    public void setMaxHealth(int currentHp) {
        hp.setMaximalHealthpoints(currentHp);
    }

    public long getLevel() {
        return xpCmp.getCurrentLevel();
    }

    public void setLevel(long lvl) {
        xpCmp.setCurrentLevel(lvl);
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public static boolean isDead() {
        return dead;
    }

    public static void setDead(boolean dead) {
        Hero.dead = dead;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public void setPosition(PositionComponent position) {
        this.position = position;
    }
}
