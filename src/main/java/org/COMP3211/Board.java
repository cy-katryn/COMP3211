package org.COMP3211;
public class Board {
    public static final int ROWS = 9;
    public static final int COLS = 7;

    public enum Terrain { RIVER, TRAP, DEN }
    private static final Terrain[][] terrain = new Terrain[ROWS][COLS];

    static {
        // set trap positions
        terrain[0][2] = Terrain.TRAP;
        terrain[0][4] = Terrain.TRAP;
        terrain[1][3] = Terrain.TRAP;

        terrain[8][2] = Terrain.TRAP;
        terrain[8][4] = Terrain.TRAP;
        terrain[7][3] = Terrain.TRAP;
        
        // set river positions
        terrain[3][1] = Terrain.RIVER;
        terrain[3][2] = Terrain.RIVER;
        terrain[4][1] = Terrain.RIVER;
        terrain[4][2] = Terrain.RIVER;
        terrain[5][1] = Terrain.RIVER;
        terrain[5][2] = Terrain.RIVER;

        terrain[3][4] = Terrain.RIVER;
        terrain[3][5] = Terrain.RIVER;
        terrain[4][4] = Terrain.RIVER;
        terrain[4][5] = Terrain.RIVER;
        terrain[5][4] = Terrain.RIVER;
        terrain[5][5] = Terrain.RIVER;

        // set den positions
        terrain[0][3] = Terrain.DEN;
        terrain[8][3] = Terrain.DEN;
    }

    public static int isTrap(int row, int col) { 
        if (terrain[row][col] == Terrain.TRAP) {
            if (row <= 1) return 1;
            else return 2; 
        }  
        return 0;
    }

    public static int isDen(int row, int col) { 
        if (terrain[row][col] == Terrain.DEN) {
            if (row == 0) return 1;
            else return 2; 
        }  
        return 0;
    }
    
    public static boolean isRiver(int row, int col) { return terrain[row][col] == Terrain.RIVER;} 
    public static boolean inBounds(int row, int col){ return row >= 0 && row < ROWS && col >= 0 && col < COLS; }
}
