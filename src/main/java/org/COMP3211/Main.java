public class Main {
    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        BoardRenderer view = new BoardRenderer();
        GameController controller = new GameController(board, view);
        controller.main();
    }
}