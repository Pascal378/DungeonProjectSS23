package starter;

import ecs.entities.Entity;
import java.io.Serializable;
import java.util.Set;

/** Data Object which stores all the objects and information needed to save the state of the game */
public class SaveData implements Serializable {
    private Set<Entity> entities;
    private int lvl;
    private long heroLvl;

    private int maxHp;
    private int currentHp;

    /**
     * @param entities - Entities on LevelLoad
     * @param lvl - Current levelDepth
     * @param heroLvl - Current level of the hero
     * @param maxHp - Current maximum hp of the hero
     * @param currentHp - Current hp of the hero
     */
    public SaveData(Set<Entity> entities, int lvl, long heroLvl, int maxHp, int currentHp) {
        this.entities = entities;
        this.lvl = lvl;
        this.heroLvl = heroLvl;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
    }

    public SaveData(int lvl) {
        this.lvl = lvl;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public long getHeroLvl() {
        return heroLvl;
    }

    public void setHeroLvl(long heroLvl) {
        this.heroLvl = heroLvl;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
}
