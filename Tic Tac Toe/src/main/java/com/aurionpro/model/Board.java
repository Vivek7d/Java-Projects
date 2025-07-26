package com.aurionpro.model;

import com.aurionpro.exception.CellAlreadyMarkedException;

public class Board {
	private Cell[] cells;

	public Board() {
		cells = new Cell[9];
		for (int i = 0; i < 9; i++) {
			cells[i] = new Cell();
		}
	}

	public boolean isBoardFull() {
		for (Cell cell : cells) {
			if (cell.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public Cell[] getCells() {
		return cells;
	}

	public void setCellMark(int loc, MarkType mark) throws CellAlreadyMarkedException {
		if (loc >= 0 && loc < 9) {
			cells[loc].setMark(mark);
		} else {
			throw new IllegalArgumentException("Invalid board location: " + loc);
		}
	}

}
