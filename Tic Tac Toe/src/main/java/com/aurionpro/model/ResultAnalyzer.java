package com.aurionpro.model;

public class ResultAnalyzer {
	private Board board;
	private ResultType result;


    public ResultAnalyzer(Board board) {
        this.board = board;
        this.result = ResultType.PROGRESS;
    }
	private boolean checkHorizontalWin() {
		Cell[] c = board.getCells();

		if (check(c, 0, 1, 2)) {
			return true;
		} else if (check(c, 3, 4, 5)) {
			return true;
		} else if (check(c, 6, 7, 8)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkVerticalWin() {
		Cell[] c = board.getCells();

		if (check(c, 0, 3, 6)) {
			return true;
		} else if (check(c, 1, 4, 7)) {
			return true;
		} else if (check(c, 2, 5, 8)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean check(Cell[] c, int i, int j, int k) {
		if (!c[i].isEmpty()) {
			if (c[i].getMark() == c[j].getMark()) {
				if (c[j].getMark() == c[k].getMark()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkDiagonalWin() {
		Cell[] c = board.getCells();

		if (check(c, 0, 4, 8)) {
			return true;
		} else if (check(c, 2, 4, 6)) {
			return true;
		} else {
			return false;
		}
	}

	public ResultType analyzeResult() {
	    if (checkHorizontalWin() || checkVerticalWin() || checkDiagonalWin()) {
	        return ResultType.WIN;
	    } else if (board.isBoardFull()) {
	        return ResultType.DRAW;
	    } else {
	        return ResultType.PROGRESS;
	    }
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public ResultType getResult() {
		return result;
	}

	public void setResult(ResultType result) {
		this.result = result;
	}

}
