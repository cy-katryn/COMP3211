package org.COMP3211;

import java.util.Hashtable;
import java.util.Map;

public class Game {
  public String player1;
  public String player2;
  public int turn = 0;

  public final Piece[][] boardPieces = new Piece[Board.ROWS][Board.COLS];
  public final Map<String, Piece> pieces = new Hashtable<>();
  public String getCurrentPlayer() { return turn % 2 == 0? player1 : player2; }

  public Game() { 
    
    Object[][] spec = {
      { "R1" , Type.RAT,      1, 2, 0 },
      { "C1" , Type.CAT,      1, 1, 5 },
      { "D1" , Type.DOG,      1, 1, 1 },
      { "W1" , Type.WOLF,     1, 2, 4 },
      { "P1" , Type.LEOPARD,  1, 2, 2 },
      { "T1" , Type.TIGER,    1, 0, 6 },
      { "L1" , Type.LION,     1, 0, 0 },
      { "E1" , Type.ELEPHANT, 1, 2, 6 },

      // instantiate player 2 pieces
      { "R2" , Type.RAT,      2, 6, 6 },
      { "C2" , Type.CAT,      2, 7, 1 },
      { "D2" , Type.DOG,      2, 7, 5 },
      { "W2" , Type.WOLF,     2, 6, 2 },
      { "P2" , Type.LEOPARD,  2, 6, 4 },
      { "T2" , Type.TIGER,    2, 8, 0 },
      { "L2" , Type.LION,     2, 8, 6 },
      { "E2" , Type.ELEPHANT, 2, 6, 0 }
    };

    for (Object[] s : spec) {
      Piece p = (new Piece((Type) s[1], (int) s[2], (int) s[3], (int) s[4]));
      boardPieces[(int) s[3]][(int) s[4]] = p;
      pieces.put((String) s[0], p);
    }
  }

  public Piece getPieceAt(int row, int col) {
    if (!Board.inBounds(row, col)) return null;
    return boardPieces[row][col];
  }
  
  public boolean movePiece(String pieceKey, String dir) {

    Piece piece = pieces.get(pieceKey + ((turn % 2) + 1)); // get piece based on current player
    
    if (piece == null) {
      System.out.println("No such piece: " + pieceKey);
      return false; // no such piece
    }

    switch (dir) {
      case "R": 
          return piece.move(0, 1);
      case "L": 
          return piece.move(0, -1);
      case "U": 
          return piece.move(-1, 0);
      case "D": 
          return piece.move(1, 0);
      default:
          System.out.println("Invalid direction: " + dir);
          return false;
    }
  }
} 