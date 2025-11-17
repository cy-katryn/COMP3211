package org.COMP3211.View;

import org.COMP3211.Controller.Main;
import org.COMP3211.Model.Board;

public class View {
    
    public static void printStartPage() {
        System.out.println("-------Welcome to Jungle Game!--------\n");
        System.out.println("=== In-game commands ===");
        System.out.println("move {key} {dir} : Move piece with key in direction dir (U/D/L/R)");
        System.out.println("undo             : Undo last move");
        System.out.println("record {filename}: Save game record to filename (default: game_record)");
        System.out.println("save {filename}  : Save current game to filename (default: game_save)");
    }

    public static void printMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("Please select your choice: ");
        System.out.println("[1] New Game");
        System.out.println("[2] Replay Game Record");
        System.out.println("[3] Reload Previous Game");
        System.out.println("[4] Exit");
        System.out.print("Enter your choice: ");
    }

    public static void printBoard() {
        for(int i = 0; i < Board.ROWS; i++){
            StringBuilder line0 = new StringBuilder();
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            
            for(int j = 0; j < Board.COLS; j++){
                String[] cell = cellBlock(i, j);
                line0.append(cell[0]);
                line1.append(cell[1]);
                line2.append(cell[2]);  
            }

            System.out.println(line0);
            System.out.println(line1);
            System.out.println(line2);
        }
    }

    public static void printCurrentPlayer() {
        String currentPlayer = Main.game.getCurrentPlayer();
        if (currentPlayer != null) {
            System.out.println("Current Player: " + currentPlayer);
        } else {
            System.out.println("No current player set.");
        }
    }

    private static String[] cellBlock(int row, int col) {
        String[] cell;
        if (Board.isTrap(row, col) != 0) {
            cell = new String[] { "+xxxxxx+", "|      |", "+xxxxxx+" };
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
