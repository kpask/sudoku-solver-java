package com.example.sudokusolver.model;

import com.example.sudokusolver.model.SudokuLoader;
import java.io.FileNotFoundException;

public class SudokuLoaderTest {
    public static void main(String[] args) throws FileNotFoundException {
        SudokuLoader.load("text.txt");
    }
}
