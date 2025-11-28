package com.sudokusolver.logic;

import com.sudokusolver.model.SudokuPuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SudokuLoader {
    public static List<SudokuPuzzle> load(String filename) throws FileNotFoundException {
        List<SudokuPuzzle> puzzles = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(filename))) {
            while (reader.hasNext()) {
                String id = reader.next();
                String puzzle = reader.next();
                String difficulty = reader.next();
                int[][] board = new int[9][9];

                for (int row = 0; row < 9; ++row) {
                    for (int col = 0; col < 9; ++col) {
                        board[row][col] = Character.getNumericValue(puzzle.charAt(row * 9 + col));
                    }
                }
                puzzles.add(new SudokuPuzzle(id, board, difficulty));
            }
        }
        return puzzles;
    }
}
