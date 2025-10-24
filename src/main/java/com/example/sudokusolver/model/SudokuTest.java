package com.example.sudokusolver.model;

import java.io.FileNotFoundException;

public class SudokuTest {
    public static void main(String[] args) throws FileNotFoundException {
        int box[][] = SudokuLoader.load("text.txt");
        SudokuBoard board = new SudokuBoard(box);
        board.printBoard();
        board.solve();
        board.printBoard();
    }
}
