package org.COMP3211;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Records {
    private final String player1Name;
    private final String player2Name;
    private final String gameDate;
    private final String startTime;
    private final List<MoveRecord> records;
    private String winner;

    public Records(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.gameDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.records = new ArrayList<>();
    }

    public void gameResult(String winner){
        this.winner = winner;
    }

    //add move records
    public void addMove(String playerName, String pieceKey, String dir, int fromRow, int fromCol, int toRow, int toCol, Type pieceType, Type capturedPiece) {
        MoveRecord move = new MoveRecord(Main.game.turn,  playerName, pieceKey, dir, fromRow, fromCol, toRow, toCol, pieceType, capturedPiece);
        records.add(move);
    }

    public void addMove(MoveRecord move) {
        if (move != null) records.add(move);
    }

    //getters
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public String getGameDate() { return gameDate; }
    public String getStartTime() { return startTime; }
    public String getWinner(){ return winner; }
    public List<MoveRecord> getRecords(){ return records; }
}