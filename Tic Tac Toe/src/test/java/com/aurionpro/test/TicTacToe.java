package com.aurionpro.test;

import java.util.Scanner;

import com.aurionpro.exception.InvalidCellPositionException;
import com.aurionpro.exception.InvalidInputException;
import com.aurionpro.model.*;

public class TicTacToe {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Player 1 Name:");
        Player player1 = null;
        while (player1 == null) {
            try {
                String name1 = scanner.nextLine().trim();
                validateName(name1);
                player1 = new Player(name1);
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage() + " Please enter a valid name (letters and spaces only).");
            }
        }

        System.out.println("Enter Player 2 Name:");
        Player player2 = null;
        while (player2 == null) {
            try {
                String name2 = scanner.nextLine().trim();
                validateName(name2);
                player2 = new Player(name2);
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage() + " Please enter a valid name (letters and spaces only).");
            }
        }

        Player[] players = { player1, player2 };

        boolean playAgain = true;

        while (playAgain) {
           
            MarkType player1Mark = null;
            while (player1Mark == null) {
                System.out.print(player1.getName() + ", choose your mark (X/O): ");
                String markInput = scanner.nextLine().trim().toUpperCase();
                if (markInput.equals("X")) {
                    player1Mark = MarkType.X;
                    player2.setMark(MarkType.O);
                } else if (markInput.equals("O")) {
                    player1Mark = MarkType.O;
                    player2.setMark(MarkType.X);
                } else {
                    System.out.println("Invalid input. Please choose either X or O.");
                }
            }
            player1.setMark(player1Mark);

            
            Board board = new Board();
            ResultAnalyzer analyzer = new ResultAnalyzer(board);
            Game game = new Game(players, board, analyzer);

            System.out.println("\nNew game has started!");
            printBoard(game.getBoard());

            while (game.getResult() == ResultType.PROGRESS) {
                System.out.println(game.getCurrentPlayer().getName() + "'s turn (" + game.getCurrentPlayer().getMark() + ")");
                
                System.out.print("Enter a location (1-9): ");
                try {
                    int loc = Integer.parseInt(scanner.nextLine());
                    validatePosition(loc);
                    game.play(loc - 1);
                    printBoard(game.getBoard());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
                } catch (InvalidCellPositionException e) {
                    System.out.println(e.getMessage());
                }

            }

            if (game.getResult() == ResultType.WIN) {
                System.out.println(game.getCurrentPlayer().getName() + " wins!");
            } else if (game.getResult() == ResultType.DRAW) {
                System.out.println("It's a draw!");
            }

            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            playAgain = response.equals("yes");
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }

    private static void printBoard(Board board) {
        System.out.println("-------------");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                System.out.print("| ");
            }

            if (board.getCells()[i].getMark() == MarkType.EMPTY) {
                System.out.print((i + 1) + " | ");
            } else {
                System.out.print(board.getCells()[i].getMark() + " | ");
            }

            if ((i + 1) % 3 == 0) {
                System.out.println("\n-------------");
            }
        }
    }
    private static void validateName(String name) throws InvalidInputException {
        if (name.isEmpty()) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidInputException("Name must contain only alphabets and spaces.");
        }
    }
    private static void validatePosition(int pos) throws InvalidCellPositionException {
        if (pos < 1 || pos > 9) {
            throw new InvalidCellPositionException("Invalid cell number! Please enter a number between 1 and 9.");
        }
    }


}
