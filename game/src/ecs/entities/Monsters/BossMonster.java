package ecs.entities.Monsters;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.heroLastPosition;
import ecs.components.ai.transition.InstantAttack;
import ecs.components.skill.*;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.entities.Monster;
import ecs.graphic.Animation;
import tools.Point;

public class BossMonster extends Monster{

    private final String pathToIdleLeft = "DK/idleLeft";
    private final String pathToIdleRight = "DK/idleRight";
    private final String pathToRunLeft = "DK/runLeft";
    private final String pathToRunRight = "DK/runRight";
    private final String onHit = "DK/runRight";
    private final int fireballCoolDown = 1;
    private float xSpeed = 0.1f;
    private float ySpeed = 0.1f;
    private int dmg = 7;
    private HealthComponent hp;
    private boolean secondPhase = false;
    private PositionComponent position;
    private Hero hero;
    private Entity heroEntity;
    private Skill firstSkill;
    private Skill secondSkill;
    private SkillComponent sCp;

    private boolean attack = false;

    private float attackrange = 100f;

    public BossMonster(Hero hero, Entity heroEntity){
        super();
        this.heroEntity = heroEntity;
        this.hero = hero;
        this.position = new PositionComponent(this);
    new AIComponent(this,
        new CollideAI(2f),
        new heroLastPosition(2),
        new InstantAttack());
        //protect.isInFightMode(this);
        setupHealthComponent();
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        setupSkillComponent();
    // Because of a bug I have to put the creation of the skills into an if condition that only
    // activates if the hero is in @attackrang
      System.out.println("Fight______________________________________________________________");
            setupEnrageSkill();
            setupFireballSkill();
            setupMeleeAI();
    }



    public void secondPhase(){
        if (hp.getCurrentHealthpoints() == 50){
            new MeleeAI(attackrange,firstSkill).fight(this);
            setxSpeed(1.2f);
            setySpeed(1.2f);
        }
    };



    public void setupMeleeAI(){
        new MeleeAI(attackrange,secondSkill).fight(this);
    }
    private void setupHealthComponent() {
        Animation hit = AnimationBuilder.buildAnimation(onHit);
        this.hp = new HealthComponent(this, 100, this::onDeath, hit, hit);
    }

    public void onDeath(Entity entity){

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
        new HitboxComponent(this, (you, other, direction) -> doDmg(other), null);
    }

    private void doDmg(Entity other) {
        if (other.getComponent(HealthComponent.class).isPresent()) {
            HealthComponent ofE = (HealthComponent) other.getComponent(HealthComponent.class).get();
            ofE.receiveHit(new Damage(this.getDmg(), DamageType.PHYSICAL, this));
        }
    }
    private void setupSkillComponent() {
        sCp = new SkillComponent(this);
    }
    private void setupEnrageSkill() {
        firstSkill = new Skill(new EnrageSkill(),10);
        sCp.addSkill(firstSkill);
    };


    private void setupFireballSkill() {
    secondSkill =
        new Skill(
            new FireballSkill(
                new ITargetSelection() {
                  @Override
                  public Point selectTargetPoint() {
                      return hero.getPosition().getPoint();
                  }
                }),
            10);
        sCp.addSkill(secondSkill);
    };


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

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }
}
