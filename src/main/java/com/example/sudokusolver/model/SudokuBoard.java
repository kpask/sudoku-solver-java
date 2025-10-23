package com.example.sudokusolver.model;

public class SudokuBoard {

    private SudokuCell[][] board = new SudokuCell[9][9];

    public SudokuBoard(int[][] grid) {
        for(int i = 0; i < 9; ++i){
            for(int j = 0; j < 9; ++j){
                board[i][j] = new SudokuCell(grid[i][j]);
            }
        }
    }

    public int getColumn(int row, int column){
        return board[row][column].getCell();
    }

    public void setColumn(int row, int column, int value){
        board[row][column].setCell(value);
    }
}
