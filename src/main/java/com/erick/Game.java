package com.erick;



public class Game {
    /*
     * Game Of Life Rules
     * -------------------
     * B3/S23
     * 
     * 1. Any dead cell with exactly three live neighbours becomes a live cell
     * 2. Any live cell with two or three live neighbours lives on to the next
     * generation.
     * 
     */
    public record Cell(int x, int y) {}

    // private final char BLACK_SQUARE = (char) 9632;
    // private final char WHITE_SQUARE = '_';
    private boolean[][] grid;
    private Cell[] initialState;
    private int generation = 0;

    public Game(int gridSize, Cell[] initialState) {
        this.grid = new boolean[gridSize][gridSize];
        this.initialState = initialState;
        initializeGrid();
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public int getGeneration() {
        return generation;
    }

    public void reset() {
        for (int y = 0; y < grid.length - 1; y++) {
            for (int x = 0; x < grid[y].length - 1; x++) {
                grid[x][y] = false;
            }
        }

        initializeGrid();
    }

    private void initializeGrid() {
        for (Cell cell : initialState) {
            grid[cell.x][cell.y] = true;
        }

        generation = 0;
    }

    // private void printCurrentState() {
    //     final int MAX = grid.length - 1;

    //     for (int y = 0; y <= MAX; y++) {
    //         for (int x = 0; x < grid[y].length; x++) {
    //             if (grid[x][y]) {
    //                 System.out.print(BLACK_SQUARE + " ");
    //             } else {
    //                 System.out.print(WHITE_SQUARE + " ");
    //             }
                
    //             if (x == MAX) {
    //                System.out.print("\n");
    //             }
    //         }
    //     }

    //     System.out.println("\n------------------------------\n");
    // }

    public void calculateNextGen() {
        boolean[][] newGrid = new boolean[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            newGrid[i] = grid[i].clone();
        }

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int aliveNeigh = countAliveNeigh(x, y);
                // If the cell is dead and has exactly 3 alive neighboors, it revives
                if (!grid[x][y] && aliveNeigh == 3) {
                    newGrid[x][y] = true;
                // If the cell is alive and has 2 or 3 alive neighboors, it stays alive
                } else if (grid[x][y] && (aliveNeigh == 2 || aliveNeigh == 3)) {
                    continue;
                // Else, the cell dies
                } else {
                    newGrid[x][y] = false;
                }
            }
        }

        // Update original grid
        for (int i = 0; i < newGrid.length; i++) {
            grid[i] = newGrid[i].clone();
        }

        generation++;
    }

    private int countAliveNeigh(int x, int y) {
        int aliveNeigh = 0;
        
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // Skip if it's the center cell or if it reached a boundary
                if (i == x && j == y || i < 0 || i > grid.length - 1 || j < 0 || j > grid[i].length - 1) {
                    continue;
                } else if (grid[i][j]) {
                    aliveNeigh++;
                }
            }
        }

        return aliveNeigh;
    }
}
