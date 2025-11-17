package org.COMP3211.Model.Save;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Writer to file (used in both record & save)
public class Write {
    public static void writeHeader(PrintWriter writer, Record record) {
        writer.println("---- Game Record ----");
        writer.println("Generated on: " + LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        writer.println("File Format Version: 1.0");
        writer.println();
    }

    public static void writePlayerInfo(PrintWriter writer, Record record) {
        writer.println("== Player Info ==");
        writer.println("Player1: " + record.getPlayer1Name());
        writer.println("Player2: " + record.getPlayer2Name());
        writer.println();
    }

    public static void writeGameSettings(PrintWriter writer, Record record) {
        writer.println("== Game ==");
        writer.println("Date: " + record.getGameDate());
        writer.println("StartTime: " + record.getStartTime());
        writer.println();
    }

    public static void writeMovementHistory(PrintWriter writer, Record record) {
        writer.println("== Game History ==");
        for (Move move : record.getRecords()) {
            writer.println(move.toFileFormat());
        }
        writer.println();
    }  

    public static void writeGameResult(PrintWriter writer, Record record) {
        writer.println("== Result ==");
        if (record.getWinner() != null) {
            writer.println("Winner: " + record.getWinner());
        } else {
            writer.println("Winner: None, game incomplete");
        }
    }
}