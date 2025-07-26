package com.aurionpro.model;

import com.aurionpro.exception.CellAlreadyMarkedException;

public class Cell {
	private MarkType mark;

	public Cell() {
		this.mark = MarkType.EMPTY;
	}

	public boolean isEmpty() {
		return this.mark == MarkType.EMPTY;
	}

	public MarkType getMark() {
		return this.mark;
	}

	public void setMark(MarkType mark) throws CellAlreadyMarkedException {
		if (!isEmpty()) {
			throw new CellAlreadyMarkedException("Cell is already marked with " + this.mark);
		}
		this.mark = mark;
	}
}
