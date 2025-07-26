package com.aurionpro.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ResultAnalyzerTest {

	private Board board;
	private ResultAnalyzer analyzer;

	@BeforeEach
	public void setup() {
		board = new Board(); 
		analyzer = new ResultAnalyzer(board);
	}

	@Test
	public void testHorizontalWiCase1() throws Exception {
		board.getCells()[0].setMark(MarkType.X);
		board.getCells()[1].setMark(MarkType.X);
		board.getCells()[2].setMark(MarkType.X);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testHorizontalWinCase2() throws Exception {
		board.getCells()[3].setMark(MarkType.X);
		board.getCells()[4].setMark(MarkType.X);
		board.getCells()[5].setMark(MarkType.X);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testHorizontalWinCase3() throws Exception {
		board.getCells()[6].setMark(MarkType.X);
		board.getCells()[7].setMark(MarkType.X);
		board.getCells()[8].setMark(MarkType.X);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testVerticalWinCase1() throws Exception {
		board.getCells()[0].setMark(MarkType.O);
		board.getCells()[3].setMark(MarkType.O);
		board.getCells()[6].setMark(MarkType.O);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testVerticalWinCase2() throws Exception {
		board.getCells()[1].setMark(MarkType.O);
		board.getCells()[4].setMark(MarkType.O);
		board.getCells()[7].setMark(MarkType.O);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testVerticalWinCase3() throws Exception {
		board.getCells()[2].setMark(MarkType.O);
		board.getCells()[5].setMark(MarkType.O);
		board.getCells()[8].setMark(MarkType.O);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}

	@Test
	public void testDiagonalWinCase1() throws Exception {
		board.getCells()[0].setMark(MarkType.X);
		board.getCells()[4].setMark(MarkType.X);
		board.getCells()[8].setMark(MarkType.X);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}
	@Test
	public void testDiagonalWinCase2() throws Exception {
		board.getCells()[2].setMark(MarkType.X);
		board.getCells()[4].setMark(MarkType.X);
		board.getCells()[6].setMark(MarkType.X);

		assertEquals(ResultType.WIN, analyzer.analyzeResult());
	}

	@Test
	public void testDrawCase1() throws Exception {
		MarkType[] marks = {
			MarkType.X, MarkType.O, MarkType.X,
			MarkType.X, MarkType.X, MarkType.O,
			MarkType.O, MarkType.X, MarkType.O
		};
		for (int i = 0; i < 9; i++) {
			board.getCells()[i].setMark(marks[i]);
		}

		assertEquals(ResultType.DRAW, analyzer.analyzeResult());
	}
	@Test
	public void testDrawCase2() throws Exception {
		MarkType[] marks = {
			MarkType.X, MarkType.O, MarkType.X,
			MarkType.O, MarkType.X, MarkType.O,
			MarkType.O, MarkType.X, MarkType.O
		};
		for (int i = 0; i < 9; i++) {
			board.getCells()[i].setMark(marks[i]);
		}

		assertEquals(ResultType.DRAW, analyzer.analyzeResult());
	}
	public void testDrawCase3() throws Exception {
		MarkType[] marks = {
			MarkType.X, MarkType.O, MarkType.X,
			MarkType.X, MarkType.O, MarkType.O,
			MarkType.O, MarkType.X, MarkType.O
		};
		for (int i = 0; i < 9; i++) {
			board.getCells()[i].setMark(marks[i]);
		}

		assertEquals(ResultType.DRAW, analyzer.analyzeResult());
	}
	@Test
	public void testProgress() throws Exception {
		board.getCells()[0].setMark(MarkType.X);
		board.getCells()[1].setMark(MarkType.O);

		assertEquals(ResultType.PROGRESS, analyzer.analyzeResult());
	}
}
