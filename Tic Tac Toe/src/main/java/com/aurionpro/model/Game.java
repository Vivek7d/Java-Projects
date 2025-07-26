package com.aurionpro.model;

import com.aurionpro.exception.CellAlreadyMarkedException;

public class Game {
    private Player currentPlayer;
    private Player[] players;
    private Board board;
    private ResultAnalyzer analyzer;
    private ResultType result = ResultType.PROGRESS;

    public Game(Player[] players, Board board, ResultAnalyzer analyzer) {
        this.players = players;
        this.board = board;
        this.analyzer = analyzer;
        this.currentPlayer = players[0]; 
        this.players[0].setMark(MarkType.X);
        this.players[1].setMark(MarkType.O);
    }

    public void play(int loc) {
        try {
            board.setCellMark(loc, currentPlayer.getMark());
            result = analyzer.analyzeResult();
            if (result == ResultType.PROGRESS) {
                switchCurrentPlayer();
            }
        } catch (CellAlreadyMarkedException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void switchCurrentPlayer() {
        if (currentPlayer == players[0]) {
            currentPlayer = players[1];
        } else {
            currentPlayer = players[0];
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ResultType getResult() {
        return result;
    }

    public Board getBoard() {
        return board;
    }
}