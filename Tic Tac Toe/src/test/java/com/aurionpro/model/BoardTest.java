package com.aurionpro.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.aurionpro.exception.CellAlreadyMarkedException;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void newBoardShouldNotBeFull() {
        assertFalse(board.isBoardFull(), "A new board should not be full.");
    }

    @Test
    void shouldSetMarkOnBoard() throws CellAlreadyMarkedException {
        board.setCellMark(0, MarkType.X);

        assertFalse(board.isBoardFull(), "The board should not be full after marking one cell.");
    }

    @Test
    void shouldThrowExceptionWhenCellIsAlreadyMarkedOnBoard() throws CellAlreadyMarkedException {
        board.setCellMark(3, MarkType.O);

        assertThrows(CellAlreadyMarkedException.class, () -> {
            board.setCellMark(3, MarkType.X);
        }, "Should throw CellAlreadyMarkedException when marking an already marked cell.");
    }

    @Test
    void boardShouldBeFullWhenAllCellsAreMarked() throws CellAlreadyMarkedException {
       
        for (int i = 0; i < 9; i++) {
            board.setCellMark(i, MarkType.X);
        }
        assertTrue(board.isBoardFull(), "The board should be full when all cells are marked.");
    }
}
