package org.COMP3211.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Save {
    
    // save record to file
    public static boolean saveRecord(Record record, String filename){
        if(!filename.endsWith(".record")) filename += ".record";
        if (writeToFile(record, filename)) {
            System.out.println("Record successfully saved to: " + filename);
            return true;
        } 
        System.err.println("Failed to save record.");
        return false;
    }

    // save game to file
    public static boolean saveGame(Record record, String filename) {
        if (!filename.endsWith(".jungle")) filename += ".jungle";
        if (writeToFile(record, filename)) {
            System.out.println("Game successfully save to: " + filename);
            return true;
        }
        System.err.println("Failed to save game.");
        return false;
    }

    public static boolean writeToFile(Record record, String filename){
        try(PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            //write player infos and game data to file
            Write.writeHeader(writer, record);
            Write.writePlayerInfo(writer, record);
            Write.writeGameSettings(writer, record);
            Write.writeMovementHistory(writer, record);
            Write.writeGameResult(writer, record);
            return true;
        } catch(IOException e) {
            return false;
        }
    }

}