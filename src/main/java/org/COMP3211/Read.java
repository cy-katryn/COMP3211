package org.COMP3211;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read {
    public static Records readFile(String filename) {
        Records record = null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean movementInfo = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (line.equals("== Player Info ==")) {
                    // match two players
                    String player1Line = reader.readLine();
                    String player2Line = reader.readLine();

                    if (player1Line == null || player2Line == null) {
                        System.err.println("Invalid record file: Missing player information.");
                        return null;
                    }

                    String player1Name = player1Line.split(":", 2)[1].trim();
                    String player2Name = player2Line.split(":", 2)[1].trim();
                    record = new Records(player1Name, player2Name);

                } else if (line.equals("== Game History ==")) {
                    movementInfo = true;
                } else if (line.equals("== Result ==")) {
                    movementInfo = false;
                } else if (movementInfo && record != null) {
                    // parse move record
                    MoveRecord move = MoveRecord.fromFileFormat(line);
                    record.addMove(move);
                } else if (line.startsWith("Winner:") && record != null) {
                    String winner = line.split(":", 2)[1].trim();
                    if (!winner.equals("None, game incomplete")) {
                        record.gameResult(winner);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read record: " + e.getMessage());
            return null;
        }

        return record;
    }
}
