package com.example.sudokusolver.model;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;


public class SudokuTest {
    public static void main(String[] args) throws FileNotFoundException {
        testAll("medium.txt");
    }


    public static void testAll(String filename){
        List<SudokuPuzzle> puzzles = null;
        try{
            puzzles = SudokuLoader.loadAllFromFile("medium.txt");
        } catch (FileNotFoundException er)
        {
            System.out.println("File not found");
        }

        assert puzzles != null;
        int puzzleCount = puzzles.size();

        int solvedCount = 0;
        for(SudokuPuzzle puzzle : puzzles){
            SudokuBoard board = new SudokuBoard(puzzle.board);
            board.solve();
            if(board.isSolved()) solvedCount++;
            System.out.println(solvedCount);
        }

        System.out.printf("Puzzles in file: %d  Solved: %.2f%%%n", puzzleCount, ((double) solvedCount / puzzleCount) * 100);
    }
}
