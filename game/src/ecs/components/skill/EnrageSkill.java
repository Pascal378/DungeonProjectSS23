package ecs.components.skill;

import ecs.components.HealthComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class EnrageSkill extends MagicSkill {
    int skillDuration = 5;

    Logger BossLogger = Logger.getLogger(getClass().getName());

    /**
     * This skill boosts the damage and speed of the executing entity in cost of 30% of their
     * maximum healthpoints.
     *
     * @param entity which uses the skill
     */
    @Override
    public void execute(Entity entity) {
        BossLogger.info("Enrage klick");
        HealthComponent hCp;
        VelocityComponent vCp;
        float ogX;
        float ogY;
        int ogDmg;
        Hero hero = null;
        if (entity instanceof Hero) hero = (Hero) entity;

        // Check if entity has HealthComponent and lower their health
        if (entity.getComponent(HealthComponent.class).isPresent()) {

            hCp = (HealthComponent) entity.getComponent(HealthComponent.class).get();
            int hpCost = (int) (hCp.getMaximalHealthpoints() * 0.3);
            int newHP = hCp.getCurrentHealthpoints() - hpCost;
            hCp.setCurrentHealthpoints(newHP);
        }

        // Check if entity has VelocityComponent and lower their speed
        if (entity.getComponent(VelocityComponent.class).isPresent()) {
            vCp = (VelocityComponent) entity.getComponent(VelocityComponent.class).get();
            ogX = vCp.getXVelocity();
            ogY = vCp.getYVelocity();
            vCp.setXVelocity(vCp.getXVelocity() * 2.25f);
            vCp.setYVelocity(vCp.getYVelocity() * 2.25f);
            if (hero != null) {
                ogDmg = hero.getDmg();
                hero.setDmg(hero.getDmg() * 2);
                BossLogger.info("Damage and velocity increased by 25% over 10 seconds.");
                durationTimer(vCp, ogX, ogY, hero, ogDmg);
            }
        }
    }

    // Check if Entity is Hero and then set is dmg

    /**
     * Timer that sets every modified stat back to its original when run out.
     *
     * @param vCp - VelocityComponent
     * @param ogX - Original X velocity
     * @param ogY - Original Y velocity
     * @param hero - The hero
     * @param ogDmg - Original damage
     */
    public void durationTimer(VelocityComponent vCp, float ogX, float ogY, Hero hero, int ogDmg) {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run() {
                        hero.setDmg(ogDmg);
                        vCp.setXVelocity(ogX);
                        vCp.setYVelocity(ogY);
                        BossLogger.info("Enrage effect ended.");
                    }
                },
                (long) skillDuration * 1000);
    }
}
