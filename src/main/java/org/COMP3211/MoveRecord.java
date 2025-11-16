package org.COMP3211;

public class MoveRecord {
    private int moveNum;
    private String playerName;
    private String pieceKey;
    private String dir;
    private int fromRow, fromCol;
    private int toRow, toCol;
    private Type pieceType;
    private Type capturedPiece; 

    public MoveRecord(int moveNum, String playerName, 
                     String pieceKey, String dir,
                     int fromRow, int fromCol, int toRow, int toCol,
                     Type pieceType, Type capturedPiece) {
        this.moveNum = moveNum;
        this.playerName = playerName;
        this.pieceKey = pieceKey;
        this.dir = dir;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.pieceType = pieceType;
        this.capturedPiece = capturedPiece;
    }

    //record to file format 
    // moveNum|player|pieceKey|dir|A1-B2|pieceType|capturedType
    public String toFileFormat() {
        String captureInfo = (capturedPiece != null) ? capturedPiece.name() : "-";
        return String.format("%d|%s|%s|%s|%c%d-%c%d|%s|%s",
            moveNum,
            playerName,
            pieceKey,
            dir,
            (char)('A' + fromCol), fromRow + 1, 
            (char)('A' + toCol), toRow + 1,
            pieceType.name(),
            captureInfo
        );
    } 

    //parse from file format
    public static MoveRecord fromFileFormat(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                int moveNum = Integer.parseInt(parts[0]);
                String playerName = parts[1];
                String pieceKey = parts[2];
                String dir = parts[3];
                String[] coordinates = parts[4].split("-");
                int fromCol = coordinates[0].charAt(0) - 'A';
                int fromRow = Integer.parseInt(coordinates[0].substring(1)) - 1;
                int toCol = coordinates[1].charAt(0) - 'A';
                int toRow = Integer.parseInt(coordinates[1].substring(1)) - 1;
                
                Type pieceType = Type.valueOf(parts[5]);
                Type capturedPiece = parts[6].equals("-") ? null : Type.valueOf(parts[4]);

                return new MoveRecord(moveNum, playerName, pieceKey, dir, fromRow, fromCol, toRow, toCol, pieceType, capturedPiece);
            }
        } catch (Exception e) {
            System.err.println("Failed to get movements: " + line);
        }
        return null;
    }

    //getters
    public int getMoveNum(){ return moveNum; }
    public String getPlayerName(){ return playerName; }
    public String getPieceKey() { return pieceKey; }
    public String getDir() { return dir; }
    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public Type getPieceType() { return pieceType; }
    public Type getCapturedPiece() { return capturedPiece; }
}