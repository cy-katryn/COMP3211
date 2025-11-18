package org.COMP3211.View;

import org.COMP3211.Controller.Main;
import org.COMP3211.Model.Game;
import org.COMP3211.Model.Move;
import org.COMP3211.Model.Read;
import org.COMP3211.Model.Record;

public class Replay {
    //load record to replay
    public static void replayRecord(String filename) {
        if (!filename.endsWith(".record")) return;

        Record record = Read.readFile(filename);
        if (record == null) {
            System.err.println("Failed to load record: " + filename);
            return;
        }
        replay(record, false);
    }

    public static void replayGame(String filename) {
        if (!filename.endsWith(".jungle")) return;

        Record record = Read.readFile(filename);
        if (record == null) {
            System.err.println("Failed to load game: " + filename);
            return;
        }
        replay(record, true);
    }

    //replay game / records
    public static void replay(Record record, boolean continueAfterReplay) {
        if (record == null) {
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

        for (Move move : record.getRecords()) {
            System.out.println("\nTurn " + move.getMoveNum() + " - " + move.getPlayerName());
            System.out.println("Input: " + move.getPieceKey() + " " + move.getDir());

            if (move.getPieceKey() != null && move.getDir() != null) {
                boolean success = replayGame.movePiece(move.getPieceKey(), move.getDir());
                if (!continueAfterReplay && success) View.printBoard();
                if (!success) System.out.println("Replay move failed.");
            }

        }

        if (record.getWinner() != null) {
            System.out.println("Game Over! Winner: " + record.getWinner());
        } else {
            System.out.println("Game Over! No winner recorded.");
        }

        if (continueAfterReplay) {
            System.out.println("Loaded game ready to continue. Returning to game loop...");
        } else {
            System.out.println("Returning to main menu...");
        }
    }
}
