package org.COMP3211.Model;

import org.COMP3211.Main;

public class Piece {

    private final Type type;
    // private final int initRow, initCol; 
    private int currRow, currCol; // store current coordinate
    private final int player; // owner of the piece
    private boolean isCaptured = false; // if capture should be true and deleted from board

    public Piece(Type type, int player, int row, int col) {
        this.type = type;
        this.player = player;
        this.currRow = row;
        this.currCol = col;
    }

    // getters 
    public Type getType() { return type; }
    public int getPlayer() { return player; }
    public int getRow() { return currRow; }
    public int getCol() { return currCol; }
    public int getRank() { return type.getRank(); }
    public boolean isCaptured() { return isCaptured; }
    public boolean canSwim() { return type == Type.RAT; }
    public boolean canJumpOverRiver() { return type == Type.TIGER || type == Type.LION; }

    // setters
    public void setCurrentPosition(int r, int c) { this.currRow = r; this.currCol = c; }  
    public void setCaptured(boolean isCaptured) { this.isCaptured = isCaptured; this.currRow = -1; this.currCol = -1; }

    
    // move piece to new position 
    public boolean move(int x, int y){
        int newRow = currRow + x;
        int newCol = currCol + y; 
        
        // check if new position can be moved to
        if(!Board.inBounds(newRow, newCol)) return false; // out of bounds
        if(Board.isDen(newRow, newCol) == player) return false; // can't enter own den

        // handle jumping over river (lion & tiger)
        Piece R1 = Main.game.piece1.get("R");
        Piece R2 = Main.game.piece2.get("R");

        if(Board.isRiver(newRow, newCol)) {
            if(this.canJumpOverRiver()){
                if(y == 0) {
                    newRow = currRow + x * 4; // jumping up down
                    if(R1.getCol() == this.getCol() || R2.getCol() == this.getCol()) return false; // rat blocking
            } 
                else {
                    newCol = currCol + y * 3; // jumping left right
                    if(R1.getRow() == this.getRow() || R2.getRow() == this.getRow()) return false; // rat blocking
                }
            }
            else if (!this.canSwim()) return false; // can't enter river
        }

        if(Main.game.boardPieces[newRow][newCol] != null){ // there is a piece in the new position
            Piece target = Main.game.getPieceAt(newRow, newCol); 
            if(target.getPlayer() == this.getPlayer()) return false; // can't capture own piece
            if(!this.canCapture(target)) return false; // can't capture enemy piece
            Main.game.capturedPiece = target; // capture target
        }

        // move piece 
        Main.game.boardPieces[newRow][newCol] = this;
        Main.game.boardPieces[currRow][currCol] = null;
        this.currRow = newRow;
        this.currCol = newCol;

        return true;
    }

    // attacker player will call this to check if they can capture target piece
    public boolean canCapture(Piece target) {
        // enemy piece is on player's trap
        if(target.isInTrap()) return true;

        // rat in river can't capture pieces on land & vice versa
        if(type == Type.RAT && Board.isRiver(currRow, currCol) != Board.isRiver(target.currRow, target.currCol)) return false;

        // rat can capture elephant
        if(type == Type.RAT && target.getType() == Type.ELEPHANT) return true;

        // elephant can't capture rat
        if(type == Type.ELEPHANT && target.getType() == Type.RAT) return false;

        return this.getRank() >= target.getRank();
    }

    public boolean isInTrap() {
        return Board.isTrap(currRow, currCol) == player; // check if what's being stepped on is a trap
    }

    // check if piece is in opponent's den
    public boolean isInDen() {
        return Board.isDen(currRow, currCol) == player; // check if what's being stepped on is a den
    }
}    