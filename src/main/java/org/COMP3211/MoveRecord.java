package org.COMP3211;

public class MoveRecord {
    private int moveNum;
    private String playerName;
    private int fromRow, fromCol;
    private int toRow, toCol;
    private Type pieceType;
    private Type capturedPiece; 

    public MoveRecord(int moveNum, String playerName, 
                     int fromRow, int fromCol, int toRow, int toCol,
                     Type pieceType, Type capturedPiece) {
        this.moveNum = moveNum;
        this.playerName = playerName;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.pieceType = pieceType;
        this.capturedPiece = capturedPiece;
    }

    //record to file format
    public String toFileFormat() {
        String captureInfo = (capturedPiece != null) ? capturedPiece.name() : "-";
        return String.format("%d|%s|%c%d-%c%d|%s|%s",
            moveNum,
            playerName,
            (char)('A' + fromCol), fromRow + 1, 
            (char)('A' + toCol), toRow + 1,
            pieceType.name(),
            captureInfo
        );
    } 

    //getters
    public int getmoveNum(){return moveNum;}
    public String getPlayerName(){return playerName;}
    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public Type getPieceType() { return pieceType; }
    public Type getCapturedPiece() { return capturedPiece; }

}