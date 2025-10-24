package com.example.sudokusolver.model;

import java.util.HashSet;
import java.util.Set;

public class SudokuBoard {
    private SudokuCell[][] board = new SudokuCell[9][9];
    private Set<Integer>[] rows = new HashSet[9];
    private Set<Integer>[] columns = new HashSet[9];
    private Set<Integer>[] boxes = new HashSet[9];
    private boolean filled;

    public SudokuBoard(int[][] grid) {
        // initialize sets
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            columns[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for(int i = 0; i < 9; ++i){
            for(int j = 0; j < 9; ++j){
                board[i][j] = new SudokuCell(grid[i][j]);
                if(grid[i][j] != 0){
                    columns[j].add(grid[i][j]);
                    rows[i].add(grid[i][j]);
                    boxes[getBoxIndex(i, j)].add(grid[i][j]);
                }
                filled = false;
            }
        }
    }

    public boolean removeInvalidCandidates(){
        boolean anyChange = false;
        for(int i = 0; i < 9; ++i){
            for(int j = 0; j < 9; ++j){
                SudokuCell cell = board[i][j];
                if(cell.getCell() == 0) {
                    for (int n = 1; n <= 9; ++n) {
                        if (cell.getCandidates().contains(n) && (columns[j].contains(n) || rows[i].contains(n) || boxes[getBoxIndex(i, j)].contains(n))){
                            anyChange = true;
                            cell.removeCandidate(n);
                        }
                    }
                }
            }
        }
        return anyChange;
    }

    public boolean fillSingleCandidate() {
        boolean anyChange = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell cell = board[i][j];
                if (cell.getCell() == 0 && cell.getCandidates().size() == 1) {
                    int value = cell.getCandidates().iterator().next();
                    cell.setCell(value);
                    columns[j].add(value);
                    rows[i].add(value);
                    boxes[getBoxIndex(i, j)].add(value);
                    anyChange = true;
                }
            }
        }
        return anyChange;
    }

    public boolean isSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getCell() == 0) return false;
            }
        }
        return true;
    }

    public void solve() {
        int iterationsWithoutProgress = 0;

        while (!isSolved() && iterationsWithoutProgress < 5) { // stop if stuck after 5 loops
            boolean changed = false;

            boolean removed = removeInvalidCandidates();
            boolean filledNow = fillSingleCandidate();
            if (removed || filledNow) {
                changed = true;
                iterationsWithoutProgress = 0;
            } else {
                iterationsWithoutProgress++;
            }
        }
    }



    public int getCellValue(int row, int column){
        return board[row][column].getCell();
    }

    public void setCellValue(int row, int column, int value){
        board[row][column].setCell(value);

    }

    public Set<Integer> getCandidates(int row, int column){
        return board[row][column].getCandidates();
    }
    public static int getBoxIndex(int row, int col){
        return (row / 3) * 3 + (col / 3);
    }

    public String getBoxes(int num){
        return boxes[num].toString();
    }

    public void printBoard(){
        System.out.println("——————————————————————————");
        for(int i = 0; i < 9; ++i){
            System.out.print("| ");
            for(int j = 0; j < 9; ++j){
                System.out.print(board[i][j].getCell() + " ");
                if((j+1)%3 == 0 && j!=8) System.out.print(" | ");
            }
            System.out.print("| ");
            System.out.println();
            if((i+1)%3 == 0) System.out.println("——————————————————————————");
        }
    }
}
