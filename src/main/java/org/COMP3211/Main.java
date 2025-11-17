package org.COMP3211;

import org.COMP3211.Model.Game;
import org.COMP3211.Model.Save.Save;

import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static Game game;
    private static String[] _command;
    private static Save saveManager = new Save();

    public static void main(String[] args) {

        View.printStartPage();

        while (true) {
            View.printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": 
                    game = new Game();

                    String name1 = "Player1_" + UUID.randomUUID().toString().substring(0,6);
                    String name2 = "Player2_" + UUID.randomUUID().toString().substring(0,6);

                    System.out.println("\n(Leave blank to use default name)");

                    System.out.print("Player 1 name: ");
                    String input1 = scanner.nextLine();
                    String player1 = input1.isEmpty() ? name1 : input1;
                    
                    String player2 = null;
                    while(true){
                        System.out.print("Player 2 name: ");
                        String input2 = scanner.nextLine();
                        player2 = input2.isEmpty() ? name2 : input2;
                        if(!player2.equals(player1)) break;
                        System.out.println("Name already taken by Player 1. Please enter a different name.");
                    }

                    game.setPlayerNames(player1, player2);
                    startGame();
                    break;

                case "2":
                    System.out.print("Enter the record file (.record) to replay: ");
                    String replayFile = scanner.nextLine().trim();
                    if (!replayFile.isEmpty()) saveManager.loadRecord(replayFile);
                    else System.out.println("No filename provided.");
                    break;

                case "3":
                    System.out.print("Enter the record file to replay: ");
                    String loadFile = scanner.nextLine().trim();
                    if (!loadFile.isEmpty()) {
                        saveManager.loadGame(loadFile);
                        if(game != null) startGame();
                    } else System.out.println("No filename provided.");
                    break;
 
                case "4":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid number, please enter 1~4 only.");
            }

        }
    }

    public static void endGame(String winner){

        if (winner != null) {
            System.out.println("Game Over! Winner: " + winner);
            //save record
            if (game.getGameRecord() != null) {
                game.getGameRecord().gameResult(winner);
                saveManager.saveRecord(game.getGameRecord(), "game.record");
            }
        }
        game = null;
    }

    public static void startGame(){
        if (game.getGameRecord() == null) game.start();
        System.out.println("Game started!");

        while (true) {
            View.printBoard();
            String winner = game.getWinner();
            if (winner != null) {
                endGame(winner);
                return;
            }
            View.printCurrentPlayer();

            System.out.println("Enter your Piece and Direction or 'quit' to exit: ");
            getCommand();

            switch (_command[0]) {
                case "move" -> {
                    if (game.movePiece(_command[1], _command[2])) System.out.println("Move successful.");
                    else System.out.println("Invalid move. Try again.");
                }

                case "undo" -> {
                    boolean undone = game.undoMove();
                    System.out.println(undone ? "Move undone." : "Cannot undo move.");
                }

                case "record" -> {
                    String filename = (_command.length > 1) ? _command[1] : "game_record";
                    if (game.getGameRecord() != null) {
                        saveManager.saveRecord(game.getGameRecord(), filename);
                    } else {
                        System.out.println("No record to save.");
                    }
                }

                case "save" -> {
                    String filename = (_command.length > 1) ? _command[1] : "game_save";
                    if (game.getGameRecord() != null) {
                        saveManager.saveGame(game.getGameRecord(), filename);
                    } else {
                        System.out.println("No game to save.");
                    }
                }

                case "quit" -> {
                    System.out.println("Exiting game. Thanks for playing!");
                    return;
                }

                // case "replay" -> {
                //     if (_command.length > 1) {
                //         saveManager.replay(_command[1]);
                //     } else {
                //         System.out.println("replay {filename}");
                //     }
                // }
            }
        }
    }

    private static void getCommand() {
        System.out.print("> ");
        _command = scanner.nextLine().split(" ");
    }
}