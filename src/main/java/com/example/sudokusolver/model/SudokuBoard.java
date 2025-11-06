package com.example.sudokusolver.model;

import java.util.*;

public class SudokuBoard {
    private SudokuCell[][] board = new SudokuCell[9][9];
    private Set<Integer>[] rows = new HashSet[9];
    private Set<Integer>[] columns = new HashSet[9];
    private Set<Integer>[] boxes = new HashSet[9];
    private Map<Integer, Integer>[] count = new Map[9];

    public SudokuBoard(int[][] grid) {
        // initialize sets
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            columns[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
            count[i] = new HashMap<>();
        }

        //Initialize cells for each grid part
        for(int i = 0; i < 9; ++i){
            for(int j = 0; j < 9; ++j){
                board[i][j] = new SudokuCell(grid[i][j]);
                if(grid[i][j] != 0){
                    columns[j].add(grid[i][j]);
                    rows[i].add(grid[i][j]);
                    boxes[getBoxIndex(i, j)].add(grid[i][j]);
                }
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
        updateBoxCandidates();
        return anyChange;
    }

    // Clears candidate count for each box, updating the occurrences of each candidate [1-9] in the box
    public void updateBoxCandidates(){
        for(Map<Integer, Integer> boxCounts:count){
            boxCounts.clear();
        }

        for(int i = 0; i < 9; ++i){
            for(int j = 0; j < 9; ++j){
                Set<Integer> cellCandidates = board[i][j].getCandidates();
                for(int n = 1; n <= 9; ++n){
                    int boxIndex = getBoxIndex(i, j);
                    if(cellCandidates.contains(n)){
                        count[boxIndex].put(n, count[boxIndex].getOrDefault(n, 0) + 1);
                    }
                }
            }
        }
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
        updateBoxCandidates();
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

    // return int[2] where int[0] = 1 or 2 (row or line), int[1] = row or column
    int[] straightLineCandidatesInBox(int boxNumber, int number){
        //Check if goes in a row
        final int ROW = 1;
        final int COLUMN = 2;

        int[] info = new int[2];
        int startRow = (boxNumber / 3) * 3;
        int startColumn = (boxNumber % 3) * 3;

        int[] rowContains = new int[3];
        int[] columnContains = new int[3];

        // Update to check if each row contains a value ?/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = startRow + i;
                int col = startColumn + j;
                if(board[row][col].getCandidates().contains(number)){
                    rowContains[i]++;
                    columnContains[j]++;
                };
            }
        }

        //Count in how many rows and columns it appears
        int appearedInRow = 0, appearInColumn = 0;;
        for(int i = 0; i < 3; ++i){
            if(rowContains[i] > 0)  appearedInRow++;
            if(columnContains[i] > 0)  appearInColumn++;
        }

        // If the candidate only appears once in a single row or column return true
        if(appearInColumn == 1 || appearedInRow == 1){
            info[0] = (appearedInRow == 1) ? ROW : COLUMN;
            if(info[0] == ROW){
                if(rowContains[0] > 0) info[1] = startRow;
                if(rowContains[1] > 0) info[1] = startRow + 1;
                if(rowContains[2] > 0) info[1] = startRow + 2;
            }
            else{
                if(columnContains[0] > 0) info[1] = startColumn;
                if(columnContains[1] > 0) info[1] = startColumn + 1;
                if(columnContains[2] > 0) info[1] = startColumn + 2;
            }
            return info;
        }
        return null;
    }

    public void solve() {
        // Parse simple candidates
        while(removeInvalidCandidates() || fillSingleCandidate());

        // More complicated - parsing box-line reduction
        // Iterate through each box
        for(int boxIndex = 0; boxIndex < 9; ++boxIndex){
            // Iterate through the possible values [1-9]
            for(int candidate = 1; candidate <= 9; candidate++){
                int[] result = straightLineCandidatesInBox(boxIndex,candidate);
                if(result == null) continue;

                int type = result[0];
                int globalIndex = result[1];

                // If box contains two values in a row (horizontally)
                if(type == 1)
                {
                    //Iterate through the board rows cells where two in a row was found with j value
                    for(int col = 0; col < 9; ++col)
                    {
                        //If we are not standing in the same box as where j was found in the row, eliminate j value
                        if(getBoxIndex(globalIndex, col) != boxIndex) board[globalIndex][col].removeCandidate(candidate);
                    }
                }
                // If box contains two values in a row (vertically)
                if(type == 2)
                {
                    // Iterate through the board column cells where two in a row was found
                    for(int row = 0; row < 9; ++row)
                    {
                        // If the cell is not in the same box as where j was found, remove j from the candidate in the column
                        if(getBoxIndex(globalIndex, row) != boxIndex) board[row][globalIndex].removeCandidate(candidate);
                    }
                }
            }
        }
        fillSingleCandidate();
        removeInvalidCandidates();
        updateBoxCandidates();
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
                if((j+1)%3 == 0 && j!=8) System.out.print("| ");
            }
            System.out.print("| ");
            System.out.println();
            if((i+1)%3 == 0) System.out.println("——————————————————————————");
        }
    }
}
