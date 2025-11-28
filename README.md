# Sudoku Solver (Java)

A **work-in-progress** Java application designed to solve standard 9×9 Sudoku puzzles using **logic-based techniques** exclusively.

## Project Status

The solver is not yet complete, as it does not employ brute-force backtracking. Its performance is purely a result of its implemented logical strategies.

| Difficulty | Solved Percentage | Notes |
| :---: | :---: | :--- |
| **Easy** | **100%** | Solves all easy puzzles reliably. |
| **Medium** | **98.78%** | Highly effective on medium difficulty. |
| **Very Hard** | **25%** | Performance gap highlights the need for advanced techniques. |

More complex solving techniques are planned for future releases.

-----

## Key Features

The solution is built around several custom data structures and an iterative solving engine (`board.solve()`) that cycles through constraint propagation and logical rules until no further changes can be made.

### Core Architecture

* **`SudokuCell`:** Tracks the cell's current value and maintains a `Set` of all possible **candidates** (potential numbers).
* **`SudokuBoard`:** The main engine, managing the 9x9 grid, sets for rows/columns/boxes, and implementing all solving algorithms.
* **`SudokuLoader`:** Utility for parsing puzzles from a text file into a data structure suitable for the solver.

### Implemented Techniques

The solver uses the following strategies, ordered from basic constraint propagation to more advanced subset logic:

1.  **Constraint Propagation:**
    * **Removing Invalid Candidates (`removeInvalidCandidates`):** The primary step of removing candidates based on filled numbers in the cell's row, column, or 3x3 box.
2.  **Basic Singles:**
    * **Naked Singles:** Fills a cell if it has only one remaining candidate (size of candidates is 1).
    * **Hidden Singles:** Fills a cell if one candidate number only appears once in the candidate sets of a unit (row, column, or box).
3.  **Subset Logic:**
    * **Naked Pairs (`nakedPairs`):** Identifies two cells in a unit that share the exact same two candidates, eliminating those candidates from all other cells in that unit.
4.  **Intersection/Cross-Hatching:**
    * **Box-Line Reduction (`boxLineReduction`):** If a number's candidates within a 3x3 box are confined to a single row or column, that number can be eliminated as a candidate from the rest of that row or column outside the box.

-----

## Usage Example

The `solve()` method in `SudokuBoard` is the main entry point, applying the techniques iteratively:

```java
// Example: Load a puzzle and attempt to solve
List<SudokuPuzzle> puzzles = SudokuLoader.load("puzzles.txt");
SudokuPuzzle p = puzzles.get(0);

SudokuBoard board = new SudokuBoard(p.board);
board.solve(); // Applies all logic-based rules

if (board.isSolved()) {
    System.out.println("Puzzle Solved Successfully.");
} else {
    System.out.println("Solver stuck, needs more advanced logic.");
}
board.printBoard();
```

### Input File Format

Puzzles are loaded from a standard text file where each line adheres to the format:

`<puzzleId> <81-digit puzzle string> <difficulty>`

* **81-digit string:** Represents the 9x9 grid read row-by-row.
* **`0`:** Represents an empty cell.

**Example Line:**

```
001 530070000600195000098000060800060003400803001700020006060000280000419005000080079 easy
```

-----

## ⚙️ Future Improvements

* **Advanced Techniques:** Implement X-Wing, Swordfish, and other chaining/forcing logic.
* **Optional Backtracking:** Introduce a **Depth-First Search (DFS)** with backtracking as an optional fallback mechanism to guarantee a solution for all solvable puzzles.
* **Benchmarking & Statistics:** Add utilities to automatically run large batches of puzzles and generate performance statistics.

-----