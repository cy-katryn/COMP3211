package org.COMP3211;
public class Piece {

  private final Type type;
  // private final int initRow, initCol; 
  private int currRow, currCol; // store current coordinate
  private int player; // owner of the piece
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
  // TODO: might need to delete
  public void setCurrentPosition(int r, int c) { this.currRow = r; this.currCol = c; }  
  public void setCaptured(boolean isCaptured) { this.isCaptured = isCaptured; this.currRow = -1; this.currCol = -1; }

  
  // move piece to new position 
  public boolean move(int x, int y){
    int newRow = currRow + x;
    int newCol = currCol + y; 
    boolean jump = false;
    
    // check if new position can be moved to

    if(!Board.inBounds(newRow, newCol)) return false; // out of bounds

    if(Board.isDen(newRow, newCol) == player) return false; // can't enter own den
    
    // handle jumping over river (lion & tiger)
    Piece R1 = Main.game.pieces.get("R1");
    Piece R2 = Main.game.pieces.get("R2");

    if(Board.isRiver(newRow, newCol)) {
      if(this.canJumpOverRiver()){
        if(y == 0) {
        newRow *= 3; // jumping left right
        if(R1.getRow() == this.getRow() || R2.getRow() == this.getRow()) return false; // rat blocking
      } 
        else {
          newCol *= 4; // jumping up down
          if(R1.getCol() == this.getCol() || R2.getCol() == this.getCol()) return false; // rat blocking
        }
        jump = true;
      }
      else if (!this.canSwim()) return false; // can't enter river
    }

    if(Main.game.boardPieces[newRow][newCol] != null){ // there is a piece in the new position
      Piece defender = Main.game.getPieceAt(newRow, newCol); 
      if(defender.getPlayer() == this.getPlayer()) return false; // can't capture own piece
      if(!this.canCapture(defender)) return false; // can't capture enemy piece
      defender.setCaptured(true); // capture defender
    }

    // move piece 
    Main.game.boardPieces[newRow][newCol] = this;
    Main.game.boardPieces[currRow][currCol] = null;
    this.currRow = newRow;
    this.currCol = newCol;

    System.out.println("Trying to move to: " + newRow + ", " + newCol);

    return true;
  }

  // attacker player will call this to check if they can capture defender piece
  public boolean canCapture(Piece defender) {
    // enemy piece is on player's trap
    if(defender.isInTrap()) return true;

    // rat in river can't capture pieces on land & vice versa
    if(type == Type.RAT && Board.isRiver(currRow, currCol) != Board.isRiver(defender.currRow, defender.currCol)) return false;

    // rat can capture elephant
    if(type == Type.RAT && defender.getType() == Type.ELEPHANT) return true;

    // elephant can't capture rat
    if(type == Type.ELEPHANT && defender.getType() == Type.RAT) return false;

    return this.getRank() >= defender.getRank();
  }

  public boolean isInTrap() {
    if (Board.isTrap(currRow, currCol) == player) return true; // check if what's being stepped on is a trap
    return false; 
  }

  // check if piece is in opponent's den
  public boolean isInDen() {
    if (Board.isDen(currRow, currCol) == player) return true; // check if what's being stepped on is a den
    return false; 
  }
}    