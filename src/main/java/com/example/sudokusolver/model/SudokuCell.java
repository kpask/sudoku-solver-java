package com.example.sudokusolver.model;

import java.util.HashSet;
import java.util.Set;

public class SudokuCell {
    private int value; // 0 = empty
    Set<Integer> candidates;

    public SudokuCell(int value){
        this.value = value;
        candidates = new HashSet<>();
        if (value == 0) {
            for (int i = 1; i <= 9; i++) candidates.add(i);
        }
    }
    public void setCell(int num){
        this.value = num;
        candidates.clear();
    }
    public int getCell(){
        return this.value;
    }
    public Set<Integer> getCandidates(){
        return candidates;
    }
    public void addCandidate(int value){
        if(value < 1 || value > 9) return;
        candidates.add(value);
    }
    public void removeCandidate(int value) {
        if (value < 1 || value > 9) return;
        candidates.remove(value);
    }
}
