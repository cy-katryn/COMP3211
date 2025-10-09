import java.util.Scanner;

public class GameController{
    private final GameBoard board;
    private final BoardRenderer view;
    private final InputHandler parser = new InputHandler();
    private final Scanner scanner = new Scanner(System.in);

    public GameController(GameBoard board, BoardRenderer view){
        this.board = board;
        this.view = view;
    }

    public void main() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------Welcome to Jungle Game!--------");

        while (true) { 
            System.out.println("=== Main Menu ===");
            System.out.println("Please select your choice: ");
            System.out.println("[1] Start New Game");
            System.out.println("[2] Load history game to replay");
            System.out.println("[3] Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Game is starting...");
                    startGame();
                    break;

                case "2":
                    System.out.println("Loading history game to replay...");
                    //TODO: loadHistoryGame();
                    break;   

                case "3":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid number, please enter 1~3 only.");
            }
            
        }
    }

    public void startGame(){
        System.out.println("Game started!");
        while (true) { 
            view.generateBoard(board);
            view.displayCurrentPlayer(board);
            System.out.print("Enter your move (fromRow fromCol direction) or 'Quit' to exit: ");
            String input = scanner.nextLine();
            InputHandler.Command cmd = parser.parseInput(input);

            switch (cmd.type) {
                case MOVE -> {
                    boolean success = board.movePiece(cmd.move);
                    System.out.println(success ? "Move successful." : "Invalid move. Try again.");
                }

                case QUIT -> {
                    System.out.println("Exiting game. Thanks for playing!");
                    return;
                }

                // TODO : save and load / replay
            }
        }
    }

    public void loadHistoryGame(){

    }

    public void endGame(){
        
    }

}