public class Piece {
    public enum Type {
        RAT(1, 'R'), CAT(2, 'C'), DOG(3, 'D'), WOLF(4, 'W'), LEOPARD(5, 'P'), TIGER(6, 'T'), LION(7, 'I'), ELEPHANT(8, 'E');
        
        private final int rank;
        private final char symbol; 

        Type(int r, char s) { this.rank = r; this.symbol = s; }

        public int getRank() { return rank; }
        public char getSymbol() { return symbol; }
    }

    private final Type type;
    private final Player owner;
    // private final int initRow, initCol; 
    private int currRow, currCol; // store current coordinate as {row, col}
    private boolean isCaptured = false; // if capture should be true and deleted from board
    private boolean isInWater = false;
    
    public Piece(Type type, Player owner, int row, int col) {
        this.type = type;
        this.owner = owner;
        this.currRow = row;
        this.currCol = col;
    }

    // getters 
    public Type getType() { return type; }
    public Player getOwner() { return owner; }
    public int getRow() { return currRow; }
    public int getCol() { return currCol; }
    public boolean isCaptured() { return isCaptured; }
    public boolean canSwim() { return type == Type.RAT; }
    public boolean canJumpOverRiver() { return type == Type.TIGER || type == Type.LION; }
    public boolean isInWater() { return isInWater; } 
    public int getRank() { return type.getRank(); }

    // setters - 
    // TODO: might need to delete
    public void setCurrentPosition(int r, int c) { this.currRow = r; this.currCol = c; }  
    public void setInWater(boolean inWater) { this.isInWater = inWater; }
    public void setCaptured(boolean isCaptured) { this.isCaptured = isCaptured; this.currRow = -1; this.currCol = -1; }

}    