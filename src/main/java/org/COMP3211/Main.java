package org.COMP3211;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    public static Game game;
    private static String[] _command;

    public static void main(String[] args) {

        System.out.println("-------Welcome to Jungle Game!--------");

        while (true) {
            System.out.println("=== Main Menu ===");
            System.out.println("Please select your choice: ");
            System.out.println("[1] New Game");
            System.out.println("[2] Load Save");
            System.out.println("[3] Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": 
                    game = new Game();
                    System.out.print("player 1 name: ");
                    game.player1 = scanner.nextLine();
                    System.out.print("player 2 name: ");
                    game.player2 = scanner.nextLine();
                    startGame();
                    break;

                case "2":
                    loadGame();
                    startGame();
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

    public static void loadGame(){

    }

    public static void endGame(){

    }

    public static void startGame(){
        System.out.println("Game started!");
        while (true) {

            View.printBoard();
            View.displayCurrentPlayer(); 

            System.out.println("Enter your Piece and Direction or 'Quit' to exit: ");

            getCommand();
            switch (_command[0]) {
                case "move" -> {
                // check input validity here1

                    boolean success = game.movePiece(_command[1], _command[2]);
                    if (success) game.turn++;
                    System.out.println(success ? "Move successful." : "Invalid move. Try again.");
                }

                case "quit" -> {
                    System.out.println("Exiting game. Thanks for playing!");
                    return;
                }

                // TODO : save and load / replay
            }
        }
    }

    private static void getCommand() {
        System.out.print("> ");
        _command = scanner.nextLine().split(" ");
    }
}