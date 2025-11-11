package com.sudokusolver.model;

public class SudokuPuzzle {
    public final String id;
    public final int[][] board;
    public final String difficulty;

    public SudokuPuzzle(String id, int[][] board, String difficulty) {
        this.id = id;
        this.board = board;
        this.difficulty = difficulty;
    }
}

