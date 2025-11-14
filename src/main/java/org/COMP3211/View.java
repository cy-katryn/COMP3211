package org.COMP3211;

public class View {
    public static void printBoard() {
        for(int i = 0; i < Board.ROWS; i++){
            StringBuilder line0 = new StringBuilder();
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            
            for(int j = 0; j < Board.COLS; j++){
                String[] cell = cellBlock(Main.game, i, j);
                line0.append(cell[0]);
                line1.append(cell[1]);
                line2.append(cell[2]);  
            }

            System.out.println(line0.toString());
            System.out.println(line1.toString()); 
            System.out.println(line2.toString());
        }
    }

    public static void displayCurrentPlayer() {
        String currentPlayer = Main.game.getCurrentPlayer();
        if (currentPlayer != null) {
            System.out.println("Current Player: " + currentPlayer);
        } else {
            System.out.println("No current player set.");
        }
    }

    private static String[] cellBlock(Game board, int row, int col) {
        String[] cell;
        if (Board.isTrap(row, col) != 0) {
            cell = new String[] { "+xxxxxx+", "|       |", "+xxxxxx+" };
        } else if (Board.isRiver(row, col)) {
            cell = new String[] { "+%%%%%%+", "|      |", "+%%%%%%+" };
        } else if (Board.isDen(row, col) != 0){
            cell = new String[] { "+######+", "|      |", "+######+" };
        } else {
            cell = new String[] { "+------+","|      |", "+------+" };
        }

        if (Main.game.getPieceAt(row, col) != null && !Main.game.getPieceAt(row, col).isCaptured()) {
            char symbol = Main.game.getPieceAt(row, col).getType().getSymbol();
            char owner = (char) ('0' + Main.game.getPieceAt(row, col).getPlayer());

            cell[1] = String.format("|  %c%c  |", symbol, owner);
        }
        return cell;
    }
}
