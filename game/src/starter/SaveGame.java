package starter;

import ecs.entities.Entity;
import ecs.entities.Friendly.Hero;
import ecs.entities.Monsters.BossMonster;
import ecs.entities.Monsters.Demon;
import ecs.entities.Monsters.Imp;
import ecs.entities.Monsters.Slime;
import ecs.entities.Traps.BearTrap;
import ecs.entities.Traps.Mine;

import java.io.*;
import java.util.logging.Logger;

/**
 * Saves and reads entities to/from file.
 */
public class SaveGame {
    private SaveData data;

    private transient Hero hero;

    private final Logger saveLogger = Logger.getLogger(SaveGame.class.getName());

    /**
     * Constructor that collects all the objects and information to save
     */
    public SaveGame() {

        if (Game.getHero().isPresent()) {
            this.hero = (Hero) Game.getHero().get();
        }

        this.data =
            new SaveData(
                Game.getEntitiesToAdd(),
                Game.getCurrentLvl(),
                hero.getLevel(),
                hero.getCurrentHealth(),
                hero.getMaxHealth());
        saveLogger.info("SaveGame is active");
    }

    /**
     * Updates data object
     */
    public void updateData() {
        this.data =
            new SaveData(
                Game.getEntitiesToAdd(),
                Game.getCurrentLvl(),
                hero.getLevel(),
                hero.getCurrentHealth(),
                hero.getMaxHealth());
    }

    /**
     * Write the SaveFile
     */
    public void writeSave() {
        updateData();
        try {
            FileOutputStream fos = new FileOutputStream("SaveFile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.data);
            saveLogger.info("Wrote data to SaveFile");
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Read the SaveFile
     */
    public void readSave() {
        SaveData toRead;

        try {
            FileInputStream fis = new FileInputStream("SaveFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);

            toRead = (SaveData) ois.readObject();
            Game.setCurrentLvl(toRead.getLvl());
            saveLogger.info("Read data from SaveFile");
            setupEntities(toRead);
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Setup all Entities and stats from the data collected within the DataStorage object
     *
     * @param toRead
     */
    public void setupEntities(SaveData toRead) {
        for (Entity entity : Game.getEntities()) {
            Game.removeEntity(entity);
        }

        for (Entity entity : toRead.getEntities()) {
            if (entity instanceof Demon) new Demon(toRead.getLvl());
            if (entity instanceof Imp) new Imp(toRead.getLvl());
            if (entity instanceof Slime) new Slime(toRead.getLvl());
            if (entity instanceof BossMonster) new BossMonster(hero);
            if (entity instanceof BearTrap) new BearTrap();
            if (entity instanceof Mine) new Mine();
        }

        hero.setLevel(toRead.getHeroLvl());
        hero.setMaxHealth(toRead.getMaxHp());
        hero.setCurrentHealth(toRead.getCurrentHp());

        saveLogger.info(
            "Set Hero level" + toRead.getHeroLvl() +
                " Set Max HP: " + toRead.getMaxHp() +
                " Set currentHP: " + toRead.getCurrentHp()
        );

        saveLogger.info("Finished setting up Entities from SaveFile");
    }
}
