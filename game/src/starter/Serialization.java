package starter;

import java.io.*;
import java.util.logging.Logger;

/**
 * The Highest Level state must always be saved in a file and when the game is started, the
 * information must then be taken from the file .
 */
public class Serialization implements Serializable {
    private static final long serialVersionUID = 42L;
    private int highestScore;
    private static final Logger logger = Logger.getLogger(Serialization.class.getName());

    /**
     * Constuctor*
     *
     * @param highestScore The value from the highest level achieved, then will be saved in the
     *     attribute highestScore.
     */
    public Serialization(int highestScore) {
        this.highestScore = highestScore;
    }
    /**
     * a file will be created with the name of the given filename, and save the values of the
     * Attributes.
     *
     * @param serialization an object of the class.
     * @param filename the name will be saved with entered name.
     */
    public static void writeObject(Serialization serialization, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(serialization);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            logger.severe("It can´t be read");
        }
    }
    /**
     * the values of the stored attributes will be read and returned as an object.
     *
     * @param filename the name of the stored file.
     * @return an Objekt with the saved values of the Attributes.
     */
    public static Serialization readObject(String filename) {
        Serialization serialization = null;
        try (FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            serialization = (Serialization) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            logger.severe("It can´t be created");
        }
        return serialization;
    }
    /**
     * Getter
     *
     * @return the value of the highest level .
     */
    public int getHighestScore() {
        return highestScore;
    }
    /** Setter set the value of the highest level . */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}
