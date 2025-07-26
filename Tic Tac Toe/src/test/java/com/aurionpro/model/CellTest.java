package com.aurionpro.model;

import com.aurionpro.model.Cell;
import com.aurionpro.model.MarkType;
import com.aurionpro.exception.CellAlreadyMarkedException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    public void testNewCellIsEmpty() {
        Cell cell = new Cell();
        assertTrue(cell.isEmpty(), "Cell should be empty initially");
        assertEquals(MarkType.EMPTY, cell.getMark(), "Initial mark should be EMPTY");
    }

    @Test
    public void testSetMarkOnEmptyCell() throws CellAlreadyMarkedException {
        Cell cell = new Cell();
        cell.setMark(MarkType.X);
        assertFalse(cell.isEmpty(), "Cell should not be empty after marking");
        assertEquals(MarkType.X, cell.getMark(), "Mark should be X after setting it");
    }

    @Test
    public void testSetMarkOnAlreadyMarkedCellThrowsException() throws CellAlreadyMarkedException {
        Cell cell = new Cell();
        cell.setMark(MarkType.O);
        
        CellAlreadyMarkedException thrown = assertThrows(CellAlreadyMarkedException.class, () -> {
            cell.setMark(MarkType.X);
        });

        assertEquals("Cell is already marked with O", thrown.getMessage());
    }
}
