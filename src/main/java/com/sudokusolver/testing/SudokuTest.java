package com.sudokusolver.testing;

import com.sudokusolver.logic.SudokuLoader;
import com.sudokusolver.model.SudokuBoard;
import com.sudokusolver.model.SudokuPuzzle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class SudokuTest {
    public static void main(String[] args) throws FileNotFoundException {
        String filename = "easy.txt";
        List<SudokuPuzzle> puzzles = null;
        List<SudokuPuzzle> unsolvedPuzzles = new ArrayList<>();

        try{
            puzzles = SudokuLoader.load(filename);
        } catch (FileNotFoundException er)
        {
            System.out.printf("File %s not found\n", filename);
            return;
        }

        int solvedCount = 0;
        int puzzleCount = puzzles.size();

        long startTime = System.nanoTime();
        for(SudokuPuzzle puzzle : puzzles){
            SudokuBoard board = new SudokuBoard(puzzle.board);
            board.solve();
            if(board.isSolved()){
                solvedCount++;
                System.out.println(solvedCount);
            }
        }
        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        System.out.printf("Puzzles in file (%s): %d  Solved: %.2f%%%n", filename, puzzleCount, ((double) solvedCount / puzzles.size()) * 100);
        System.out.printf("Time elapsed: %.3f ms\n", elapsedMs);
    }
}
