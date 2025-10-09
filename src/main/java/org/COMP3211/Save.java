package org.COMP3211;

import java.io.*;

/**
 * save load file system
 */
public class Save implements Serializable {
    /**
     * the current Save
     */
    public static Save Inst;
    /**
     * current Game
     */
    public Game game = new Game();
    /**
     * current turn
     */
    public int turn = 0;

    /**
     * @param path save to path
     */
    public static void diskSave(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(Inst);
            Main.output("disk and criteria saved to " + path);
        } catch (IOException e) {
            Main.output("error saving disk: " + e.getMessage());
        }
    }

    /**
     * @param path load from path
     */
    public static void diskLoad(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Inst = (Save) ois.readObject();

            Main.output("loaded from " + path);
        } catch (IOException | ClassNotFoundException e) {
            Main.output("error loading disk: " + e.getMessage());
        }
    }

}