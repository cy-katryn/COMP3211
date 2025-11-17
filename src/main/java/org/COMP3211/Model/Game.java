package org.COMP3211.Model;

import java.util.Hashtable;
import java.util.Map;

public class Game {
    public int turn = 0;
    public Piece capturedPiece = null;

    public String player1;
    public String player2;

    private Record record;
    private int undoCount = 0;
    private static final int MAX_UNDO = 3;

    public final Piece[][] boardPieces = new Piece[Board.ROWS][Board.COLS];
    public final Map<String, Piece> piece1 = new Hashtable<>();
    public final Map<String, Piece> piece2 = new Hashtable<>();

    public String getCurrentPlayer() { return turn % 2 == 0? player1 : player2; }
    public Record getGameRecord() { return record; }

    public Game() { 
        Object[][] spec = {
        { "R" , Type.RAT,      1, 2, 0 },
        { "C" , Type.CAT,      1, 1, 5 },
        { "D" , Type.DOG,      1, 1, 1 },
        { "W" , Type.WOLF,     1, 2, 4 },
        { "P" , Type.LEOPARD,  1, 2, 2 },
        { "T" , Type.TIGER,    1, 0, 6 },
        { "L" , Type.LION,     1, 0, 0 },
        { "E" , Type.ELEPHANT, 1, 2, 6 },

        // instantiate player 2 pieces
        { "R" , Type.RAT,      2, 6, 6 },
        { "C" , Type.CAT,      2, 7, 1 },
        { "D" , Type.DOG,      2, 7, 5 },
        { "W" , Type.WOLF,     2, 6, 2 },
        { "P" , Type.LEOPARD,  2, 6, 4 },
        { "T" , Type.TIGER,    2, 8, 0 },
        { "L" , Type.LION,     2, 8, 6 },
        { "E" , Type.ELEPHANT, 2, 6, 0 }
        };

        for (Object[] s : spec) {
            Piece p = (new Piece((Type) s[1], (int) s[2], (int) s[3], (int) s[4]));
            boardPieces[(int) s[3]][(int) s[4]] = p;
            if ((int) s[2] == 1) piece1.put((String) s[0], p);
            else piece2.put((String) s[0], p);
        }
    }

    public Piece getPieceAt(int row, int col) {
        if (!Board.inBounds(row, col)) return null;
        return boardPieces[row][col];
    }

    public void setPlayerNames(String name1, String name2) {
        this.player1 = name1;
        this.player2 = name2;
    }

    public void start(){
        this.record = new Record(player1, player2);
        this.turn = 0;
        this.undoCount = 0;
    }
    
    public boolean movePiece(String pieceKey, String dir) {
        Piece piece = (turn % 2 == 0) ? piece1.get(pieceKey) : piece2.get(pieceKey);
        if (piece == null) {
            System.out.println("No such piece: " + pieceKey);
            return false; // no such piece
        }

        //record initial position & piece type
        int fromRow = piece.getRow();
        int fromCol = piece.getCol();
        Type pieceType = piece.getType();

        boolean moved = false;

        switch (dir) {
        case "R": moved = piece.move(0, 1); break;
        case "L": moved = piece.move(0, -1); break;
        case "U": moved = piece.move(-1, 0); break;
        case "D": moved = piece.move(1, 0); break;
        default:
            System.out.println("Invalid direction: " + dir);
            return false;
        }

        if (!moved) return false;

        Piece captured = this.capturedPiece;
        Type capturedType = (captured != null) ? captured.getType() : null;

        if(captured != null){
            captured.setCaptured(true);
            if(turn % 2 == 0) piece2.values().remove(captured);
            else piece1.values().remove(captured);
            this.capturedPiece = null; // reset piece after move
        }

        // record the movements if piece moved
        if(record != null){
            int toRow = piece.getRow();
            int toCol = piece.getCol();
            // add move to game record
            record.addMove(getCurrentPlayer(), pieceKey, dir, fromRow, fromCol, toRow, toCol, pieceType, capturedType);
        }
        turn++; // next turn
        return true;
    }

    public boolean undoMove() {
        if(undoCount >= MAX_UNDO) {
            System.out.println("Maximum undo limit reached.");
            return false;
        }

        if (turn == 0) {
            System.out.println("No moves to undo.");
            return false;
        }

        // return to previous state and delete record
        turn--;
        Move lastMove = record.getRecords().remove(record.getRecords().size() - 1);
        Piece piece = (turn % 2 == 0) ? piece1.get(lastMove.getPieceKey()) : piece2.get(lastMove.getPieceKey());
        // move piece back to original position
        piece.move(lastMove.getFromRow() - piece.getRow(), lastMove.getFromCol() - piece.getCol());

        // if there was a captured piece, restore it
        if (lastMove.getCapturedPiece() != null) {
            Piece restoredPiece = new Piece(lastMove.getCapturedPiece(), (turn % 2 == 0) ? 2 : 1, lastMove.getToRow(), lastMove.getToCol());
            boardPieces[lastMove.getToRow()][lastMove.getToCol()] = restoredPiece;
            if (turn % 2 == 0) piece2.put(lastMove.getPieceKey(), restoredPiece);
            else piece1.put(lastMove.getPieceKey(), restoredPiece);
        } else {
            boardPieces[lastMove.getToRow()][lastMove.getToCol()] = null;
        }

        undoCount++;
        return true;
    }


    public int isGameOver() {
        // check if any player is in den
        if (boardPieces[0][3] != null && boardPieces[0][3].getPlayer() == 2) return 2;
        if (boardPieces[8][3] != null && boardPieces[8][3].getPlayer() == 1) return 1;

        // check if any player has no pieces left
        if (piece1.isEmpty()) return 2;
        if (piece2.isEmpty()) return 1;
        
        return 0;
    }
    
    public String getWinner() {
        if (isGameOver() == 1) return player1;
        if (isGameOver() == 2) return player2;
        return null;
    }
} 
