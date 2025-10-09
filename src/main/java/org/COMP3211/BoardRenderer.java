public class BoardRenderer {
    public void generateBoard(GameBoard board) {
        for(int i = 0; i < GameBoard.ROWS; i++){
            StringBuilder line0 = new StringBuilder();
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            
            for(int j = 0; j < GameBoard.COLS; j++){
                String[] cell = cellBlock(board, i, j);
                line0.append(cell[0]);
                line1.append(cell[1]);
                line2.append(cell[2]);  
            }

            System.out.println(line0.toString());
            System.out.println(line1.toString()); 
            System.out.println(line2.toString());
        }
    }

    public void displayCurrentPlayer(GameBoard board) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null) {
            System.out.println("Current Player: " + currentPlayer.getPlayerName());
        } else {
            System.out.println("No current player set.");
        }
    }

    private String[] cellBlock(GameBoard board, int row, int col) {
        String[] cell;
        if (board.isTrap(row, col)) {
            cell = new String[] { "+xxxxxx+", "|      |", "+xxxxxx+" };
        } else if (board.isRiver(row, col)) {
            cell = new String[] { "+%%%%%%+", "|      |", "+%%%%%%+" };
        } else if (board.isDen(row, col)){
            cell = new String[] { "+######+", "|      |", "+######+" };
        } else {
            cell = new String[] { "+------+","|      |", "+------+" };
        }
        
        if (board.pieceAt(row, col) != null && !board.pieceAt(row, col).isCaptured()) {
            char symbol = board.pieceAt(row, col).getType().getSymbol();
            char owner = board.pieceAt(row, col).getOwner().isFirstPlayer() ? '1' : '2';

            cell[1] = String.format("|  %c%c  |", symbol, owner);
        }
        return cell;
    }
}
