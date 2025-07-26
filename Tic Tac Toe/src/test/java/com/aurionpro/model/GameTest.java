package com.aurionpro.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

    private Player player1;
    private Player player2;
    private Player[] players;
    private Board board;
    private ResultAnalyzer analyzer;
    private Game game;

    @BeforeEach
    public void setup() {
        player1 = new Player("Alice");
        player2 = new Player("Bob");
        players = new Player[] { player1, player2 };
        board = new Board(); 
        analyzer = new ResultAnalyzer(board);
        game = new Game(players, board, analyzer);
    }

    @Test
    public void testInitialSetup() {
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(MarkType.X, player1.getMark());
        assertEquals(MarkType.O, player2.getMark());
        assertEquals(ResultType.PROGRESS, game.getResult());
    }

    @Test
    public void testPlayAndSwitchPlayer() {
        game.play(0);
        assertEquals(MarkType.X, board.getCells()[0].getMark());
        assertEquals(player2, game.getCurrentPlayer()); // Now it's Bob's turn
    }

    @Test
    public void testCellAlreadyMarkedException() {
        game.play(0); 
        game.play(0); 
        assertEquals(MarkType.X, board.getCells()[0].getMark());
        assertEquals(player2, game.getCurrentPlayer()); // Still Bob's turn, as exception doesn't switch player
    }

    @Test
    public void testWinningScenarioForPlayer1() {
        game.play(0); // X
        game.play(3); // O
        game.play(1); // X
        game.play(4); // O
        game.play(2); // X wins

        assertEquals(ResultType.WIN, game.getResult());
        assertEquals(MarkType.X, player1.getMark());
    }

    @Test
    public void testDrawScenario() {
        game.play(0); // X
        game.play(1); // O
        game.play(2); // X
        game.play(4); // O
        game.play(3); // X
        game.play(5); // O
        game.play(7); // X
        game.play(6); // O
        game.play(8); // X

        assertEquals(ResultType.DRAW, game.getResult());
    }

    @Test
    public void testInvalidMoveIndex() {
        game.play(-1); 
        game.play(9);  
        // Game should not crash
        assertEquals(ResultType.PROGRESS, game.getResult());
        assertEquals(player1, game.getCurrentPlayer());
    }
}
