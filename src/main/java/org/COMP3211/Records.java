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
    private String winner;
    private List<MoveRecord> moves;

    public Records(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.gameDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.moves = new ArrayList<>();
    }

    public void gameResult(String winner){
        this.winner = winner;
    }

    //add move records
        public void addMove(int moveNum, String playerName, int fromRow, int fromCol, int toRow, int toCol, 
                       Type pieceType, Type capturedPiece) {
        MoveRecord move = new MoveRecord(
            moves.size() + 1,  
            playerName,
            fromRow, fromCol,
            toRow, toCol,
            pieceType,
            capturedPiece
        );
        moves.add(move);
    }

    //getters
    public String getPlayer1Name() {return player1Name;}
    public String getPlayer2Name() {return player2Name;}
    public String getGameDate() {return gameDate;}
    public String getStartTime() {return startTime;}
    public String getWinner(){return winner;}
    public List<MoveRecord> getMovevs(){return moves;}
}