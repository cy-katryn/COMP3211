public class GameBoard {
  public static final int ROWS = 9;
  public static final int COLS = 7;

  public enum Terrain { RIVER, TRAP, DEN }
  private final Terrain[][] terrain = new Terrain[ROWS][COLS];
  private final Piece[][] pieces = new Piece[ROWS][COLS];

  private final Player player1;
  private final Player player2;
  private Player currentPlayer; // TODO: !Not sure yet

  public GameBoard() 
  {
    // set trap positions
    terrain[0][2] = Terrain.TRAP;
    terrain[0][4] = Terrain.TRAP;
    terrain[1][3] = Terrain.TRAP;

    terrain[8][2] = Terrain.TRAP;
    terrain[8][4] = Terrain.TRAP;
    terrain[7][3] = Terrain.TRAP;
    
    // set river positions
    terrain[3][1] = Terrain.RIVER; terrain[3][2] = Terrain.RIVER;
    terrain[4][1] = Terrain.RIVER; terrain[4][2] = Terrain.RIVER;
    terrain[5][1] = Terrain.RIVER; terrain[5][2] = Terrain.RIVER;

    terrain[3][4] = Terrain.RIVER; terrain[3][5] = Terrain.RIVER;
    terrain[4][4] = Terrain.RIVER; terrain[4][5] = Terrain.RIVER;
    terrain[5][4] = Terrain.RIVER; terrain[5][5] = Terrain.RIVER;

    // set den positions
    terrain[0][3] = Terrain.DEN;
    terrain[8][3] = Terrain.DEN;

    // intialize two players to setted names 
    player1 = new Player("Player 1", true); // first player
    player2 = new Player("Player 2", false); // second player

    // initialize pieces for both players and place them on board
    initializePieces(player1, player2);
    this.currentPlayer = player1;
  }

  public Piece pieceAt(int row, int col){ return inBounds(row, col) ? pieces[row][col] : null; }
  public Player getCurrentPlayer() { return currentPlayer; }
  public void setCurrentPlayer(Player player) { this.currentPlayer = player; }

  private void initializePieces(Player p1, Player p2) {
    Object[][] spec = {
      // instantiate player 1 pieces
      { Piece.Type.RAT,      p1, 2, 0 },
      { Piece.Type.CAT,      p1, 1, 5 },
      { Piece.Type.DOG,      p1, 1, 1 },
      { Piece.Type.WOLF,     p1, 2, 4 },
      { Piece.Type.LEOPARD,  p1, 2, 2 },
      { Piece.Type.TIGER,    p1, 0, 6 },
      { Piece.Type.LION,     p1, 0, 0 },
      { Piece.Type.ELEPHANT, p1, 2, 6 },

      // instantiate player 2 pieces
      { Piece.Type.RAT,      p2, 6, 6 },
      { Piece.Type.CAT,      p2, 7, 1 },
      { Piece.Type.DOG,      p2, 7, 5 },
      { Piece.Type.WOLF,     p2, 6, 2 },
      { Piece.Type.LEOPARD,  p2, 6, 4 },
      { Piece.Type.TIGER,    p2, 8, 0 },
      { Piece.Type.LION,     p2, 8, 6 },
      { Piece.Type.ELEPHANT, p2, 6, 0 }
    };

    for (Object[] s : spec) {
      Piece.Type type = (Piece.Type) s[0];
      Player owner = (Player) s[1];
      int r = (int) s[2];
      int c = (int) s[3];
      placePiece(new Piece(type, owner, r, c));
    }
  }

  public void placePiece(Piece p){
    if(p == null) return;
    int r = p.getRow();
    int c = p.getCol();
    if(!inBounds(r, c)) return;
    pieces[r][c] = p;
  }
  
  public boolean isTrap(int row, int col) { return terrain[row][col] == Terrain.TRAP; }
  public boolean isRiver(int row, int col) { return terrain[row][col] == Terrain.RIVER;}
  public boolean isDen(int row, int col) { return terrain[row][col] == Terrain.DEN; }
  public boolean inBounds(int row, int col){ return row >= 0 && row < ROWS && col >= 0 && col < COLS; }  

  // moving of pieces (select & move)
  public boolean movePiece(InputHandler.Move move) {
    int selectR = move.fromR, selectC = move.fromC;
    Movement dir = move.direction;

    // check validity of chess piece being moved
    if (!inBounds(selectR, selectC)) return false; // selected cell out of bounds
    Piece movePiece = pieces[selectR][selectC];
    if (movePiece == null) return false; // no piece to move
    if (movePiece.getOwner() != currentPlayer) return false; // not the owner of the piece

    // determine target cell based on direction
    int targetR = selectR, targetC = selectC;
    switch (dir) {
      case UP -> targetR = selectR - 1;
      case DOWN -> targetR = selectR + 1;
      case LEFT -> targetC = selectC - 1;
      case RIGHT -> targetC = selectC + 1;
      default -> { return false; } // invalid direction
    }

    // check if terrain can be moved onto
    // TODO: unsure if it would cause error such as leopard in water not detected!

    if(isRiver(targetR, targetC) && movePiece.canJumpOverRiver()) {
      switch (dir) {
        case UP -> targetR = selectR - 4;
        case DOWN -> targetR = selectR + 4;
        case LEFT -> targetC = selectC - 3;
        case RIGHT -> targetC = selectC + 3;
      }

      // check if there is rat in the river blocking the jump
      if (dir == Movement.UP || dir == Movement.DOWN) {
        for(int r = Math.min(selectR, targetR) + 1; r < Math.max(selectR, targetR); r++) {
          if (isRiver(r, selectC) && pieces[r][selectC] != null) {
            return false; // jump blocked by rat in river
          }
        }
      }
      else {
        for(int c = Math.min(selectC, targetC) + 1; c < Math.max(selectC, targetC); c++) {
          if (isRiver(selectR, c) && pieces[selectR][c] != null) {
            return false; // jump blocked by rat in river
          }
        }
      }
    }

    if(ownsDen(targetR, targetC, currentPlayer)) return false; // cannot move onto own den
    if(isRiver(targetR, targetC) && !movePiece.canSwim()) return false; // cannot move onto river
    if (!inBounds(targetR, targetC)) return false; // targetted cell out of bounds

    // check if there is a piece to capture and if can capture
    if(pieces[targetR][targetC] != null) { // target cell has piece to capture 
      Piece target = pieces[targetR][targetC];
      if (target.getOwner() == currentPlayer) return false; // cannot capture own piece
      else if (!canCapture(movePiece, target, targetR, targetC)) return false;
    }

    pieces[targetR][targetC] = movePiece;
    pieces[selectR][selectC] = null;
    movePiece.setCurrentPosition(targetR, targetC);
    currentPlayer = (currentPlayer == player1) ? player2 : player1; // switch turn
    return true;
  }

  public boolean canCapture(Piece attacker, Piece defender, int row, int col) {
    // enemy piece is on player's trap
    if(ownsTrap(row, col, attacker.getOwner())) return true;

    // rat in river can't capture pieces on land
    if(attacker.getType() == Piece.Type.RAT && pieces[row][col] == null) return false;

    // rat can capture elephant
    if(attacker.getType() == Piece.Type.RAT && defender.getType() == Piece.Type.ELEPHANT) return true;

    // elephant can't capture rat
    if(attacker.getType() == Piece.Type.ELEPHANT && defender.getType() == Piece.Type.RAT) return false;

    return attacker.getRank() >= defender.getRank();
  }

  public boolean ownsTrap(int row, int col, Player player) {
    if (!isTrap(row, col)) return false;
    if (player.isFirstPlayer()) {
      return row >= 0 && row <= 2; // Player 1's traps are in rows 0, 1, 2
    } else {
      return row >= 6 && row <= 8; // Player 2's traps are in rows 6, 7, 8
    }
  }

  public boolean ownsDen(int row, int col, Player player) {
    if (!isDen(row, col)) return false;
    if (player.isFirstPlayer()) {
      return row == 8; // Player 1's den is at row 8
    } else {
      return row == 0; // Player 2's den is at row 0
    }
  }

}

/*  hajimi  nanbeiludou
    :) manbomanbo~
    lalalalalalala~~
    bababibo =]
*/