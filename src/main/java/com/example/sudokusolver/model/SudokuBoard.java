package com.example.sudokusolver.model;

import java.util.*;

public class SudokuBoard {
    private SudokuCell[][] board = new SudokuCell[9][9];
    private Set<Integer>[] rows = new HashSet[9];
    private Set<Integer>[] columns = new HashSet[9];
    private Set<Integer>[] boxes = new HashSet[9];
    private Map<Integer, Integer>[] boxCandidateCount = new Map[9];

    public SudokuBoard(int[][] grid) {
        // initialize sets
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            columns[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
            boxCandidateCount[i] = new HashMap<>();
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
        for(int row = 0; row < 9; ++row){
            for(int col = 0; col < 9; ++col){
                SudokuCell cell = board[row][col];
                if(cell.getCell() == 0) {
                    for (int n = 1; n <= 9; ++n) {
                        if (cell.getCandidates().contains(n) && (columns[col].contains(n) || rows[row].contains(n) || boxes[getBoxIndex(row, col)].contains(n))){
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
    public boolean updateBoxCandidates(){
        boolean hasChanged = false;
        Map<Integer, Integer>[] boxCopy = new Map[9];
        for(int i = 0; i < 9; ++i){
            boxCopy[i] = new HashMap<>(boxCandidateCount[i]);
            boxCandidateCount[i].clear();
        }

        for(int row = 0; row < 9; ++row){
            for(int col = 0; col < 9; ++col){
                Set<Integer> cellCandidates = board[row][col].getCandidates();
                for(int n = 1; n <= 9; ++n){
                    int boxIndex = getBoxIndex(row, col);
                    if(cellCandidates.contains(n)){
                        boxCandidateCount[boxIndex].put(n, boxCandidateCount[boxIndex].getOrDefault(n, 0) + 1);
                    }
                }
            }
        }

        for(int box = 0; box < 9; ++box){
            if (!boxCandidateCount[box].equals(boxCopy[box])) {
                hasChanged = true;
                break;
            }
        }
        return hasChanged;
    }

    //fill cells that either have a single candidate, or a cell that has a candidate that only appears once in the box
    public boolean fillSingleCandidate() {
        boolean anyChange = false;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                SudokuCell cell = board[row][col];
                if (cell.getCell() == 0 && cell.getCandidates().size() == 1) {
                    int value = cell.getCandidates().iterator().next();
                    cell.setCell(value);
                    columns[col].add(value);
                    rows[row].add(value);
                    boxes[getBoxIndex(row, col)].add(value);
                    anyChange = true;
                }
            }
        }
        // Fill single candidates of boxes
        for(int boxIndex = 0; boxIndex < 9; ++boxIndex){
            for(int candidate = 1; candidate <= 9; ++candidate){
                if(boxCandidateCount[boxIndex].getOrDefault(candidate, 0) == 1){
                    int[] boxStartPos = getFirstCellPosition(boxIndex);
                    int startX = boxStartPos[0];
                    int startY = boxStartPos[1];
                    for(int row = startX; row < startX + 3; ++row){
                        for(int col = startY; col < startY + 3; ++col){
                            SudokuCell cell = board[row][col];
                            if(cell.getCell() == 0 & cell.getCandidates().contains(candidate)){
                                cell.setCell(candidate);
                                columns[col].add(candidate);
                                rows[row].add(candidate);
                                boxes[getBoxIndex(row,col)].add(candidate);
                                anyChange = true;
                            }
                        }
                    }
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
        int[] info = new int[2];

        final int ROW = 1;
        final int COLUMN = 2;

        // Get the starting coordinates of the specified box
        int[] boxStartCoordinates = getFirstCellPosition(boxNumber);
        int startRow = boxStartCoordinates[0];
        int startColumn = boxStartCoordinates[1];

        int[] rowContains = new int[3];
        int[] columnContains = new int[3];
        int appeared = 0;
        // Check how many times the specified candidate appears in each row and column
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = startRow + i;
                int col = startColumn + j;
                if(board[row][col].getCandidates().contains(number)){
                    rowContains[i]++;
                    columnContains[j]++;
                    appeared++;
                };
            }
        }

        if(appeared > 3 || appeared < 2){
            return null;
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
        while (true) {
            boolean a = removeInvalidCandidates();
            boolean b = fillSingleCandidate();
            if (!(a || b)) break;
        }


        // Box-line reduce until nothing changes
        boolean hasChanged = true;
        while(hasChanged){
            hasChanged = false;
            if(boxLineReduction()) hasChanged = true;
            if(fillSingleCandidate()) hasChanged = true;
            if(removeInvalidCandidates()) hasChanged = true;
            if(updateBoxCandidates()) hasChanged = true;
        }
    }

    public boolean boxLineReduction(){
        boolean hasChanged = false;
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
                        if(getBoxIndex(globalIndex, col) != boxIndex && board[globalIndex][col].getCandidates().contains(candidate))
                        {
                            board[globalIndex][col].removeCandidate(candidate);
                            hasChanged = true;
                        }
                    }
                }
                // If box contains two values in a row (vertically)
                if(type == 2)
                {
                    // Iterate through the board column cells where two in a row was found
                    for(int row = 0; row < 9; ++row)
                    {
                        // If the cell is not in the same box as where j was found, remove j from the candidate in the column
                        if(getBoxIndex(row, globalIndex) != boxIndex && board[row][globalIndex].getCandidates().contains(candidate))
                        {
                            board[row][globalIndex].removeCandidate(candidate);
                            hasChanged = true;
                        }

                    }
                }
            }
        }
        return hasChanged;
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

    public int[] getFirstCellPosition(int box){
        int[] pos = new int[2];
        pos[0] = (box / 3) * 3;
        pos[1] = (box % 3) * 3;
        return pos;
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

    public void printCandidates(){
        for(int row = 0; row < 9; ++row){
            for(int col = 0; col < 9; ++col){
                System.out.print(board[row][col].getCandidates() + " ");
            }
            System.out.println();
        }
    }
}
