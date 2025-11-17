package org.COMP3211.Model.Save;

import org.COMP3211.Main;
import org.COMP3211.Model.Game;
import org.COMP3211.View;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Save {
    
    // save record to file
    public boolean saveRecord(Record record, String filename){
        if(!filename.endsWith(".record")) filename += ".record";
        if (writeToFile(record, filename)) {
            System.out.println("Record successfully saved to: " + filename);
            return true;
        } 
        System.err.println("Failed to save record.");
        return false;
    }

    // save game to file
    public boolean saveGame(Record record, String filename) {
        if (!filename.endsWith(".jungle")) filename += ".jungle";
        if (writeToFile(record, filename)) {
            System.out.println("Game successfully save to: " + filename);
            return true;
        }
        System.err.println("Failed to save game.");
        return false;
    }

    public boolean writeToFile(Record record, String filename){
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

    //load record to replay
    public void loadRecord(String filename) {
        if(!filename.endsWith(".record")) return;

        Record record = Read.readFile(filename);
        if(record == null) {
            System.err.println("Failed to load record: " + filename);
            return;
        }
        replay(record, false);
    }
    public void loadGame(String filename){
        if(!filename.endsWith(".jungle")) return;
        
        Record record = Read.readFile(filename);
        if(record == null) {
            System.err.println("Failed to load game: " + filename);
            return;
        }
        replay(record, true);
    }

    //replay game / records
    public void replay(Record record, boolean continueAfterReplay){
        if(record == null){
            System.err.println("No record to replay.");
            return;
        }

        System.out.println("-----Start Replaying Game-----");
        System.out.println("It's the game: " + record.getPlayer1Name() + " VS. " + record.getPlayer2Name());
        System.out.println("Date: " + record.getGameDate() + ", Start From: " + record.getStartTime());

        //start replay game 
        Game replayGame = new Game();
        replayGame.setPlayerNames(record.getPlayer1Name(), record.getPlayer2Name());
        replayGame.start();
        Main.game = replayGame;

        for(Move move : record.getRecords()){
            System.out.println("\nTurn " + move.getMoveNum() + " - " + move.getPlayerName());
            System.out.println("Input: " + move.getPieceKey() + " " + move.getDir());

            if (move.getPieceKey() != null && move.getDir() != null) {
                boolean success = replayGame.movePiece(move.getPieceKey(), move.getDir());
                if(!continueAfterReplay && success) View.printBoard();
                if (!success) System.out.println("Replay move failed.");
            }
        
        }

        if(record.getWinner() != null){
            System.out.println("Game Over! Winner: " + record.getWinner());
        } else{
            System.out.println("Game Over! No winner recorded.");
        }

        if(continueAfterReplay){
            System.out.println("Loaded game ready to continue. Returning to game loop...");
        } else{
            System.out.println("Returning to main menu...");
        }
    }
}