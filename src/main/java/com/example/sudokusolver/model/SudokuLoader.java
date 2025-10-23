package com.example.sudokusolver.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuLoader {
    public static int[][] load(String filename) throws FileNotFoundException {
        File myObj = new File(filename);
        Scanner reader = new Scanner(myObj);
        int[][] board = new int[9][9];
        for(int i = 0; i < 9; i++){
            String line = reader.nextLine().trim();
            for(int j = 0; j < 9; j++){
                char digit = line.charAt(j);
                board[i][j]= Character.getNumericValue(digit);
            }
        }
        return board;
    }
}
