package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.*;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration{
    private char grid[][];
    private int rows;
    private int columns;

    /**
     * This method will take in the file and create a grid for the pond, the pond will be made up of characters and
     * the characters represent if there are empty spots, invalid spots, the red frog and green frogs.
     * @param filename this is the name of the file
     * @throws IOException
     */
    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields= in.readLine().split("\\s+");
            this.rows = Integer.parseInt(fields[0]);
            this.columns = Integer.parseInt(fields[1]);
            this.grid = new char[rows][columns];
            for(int r = 0; r < rows; r++){
                String[] field2 = in.readLine().split("\\s+");
                for(int c = 0; c < columns; c++){
                    grid[r][c] = field2[c].charAt(0);

                }
            }
        }
    }

    /**
     * This constructor is a copy of the grid that will be changed during configurations
     * @param other the original grid
     */
    private HoppersConfig(HoppersConfig other){
        this.rows = other.rows;
        this.columns = other.columns;
        this.grid = new char[this.rows][this.columns];
        for(int row = 0; row < rows; row++){
            System.arraycopy(other.grid[row],0,this.grid[row],0,this.columns);
        }

    }

    /**
     * This method will determine whether the configuration is the solution or not
     * @return it returns true if it is a solution and false if its not
     */
    @Override
    public boolean isSolution() {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < columns; c++){
                if(grid[r][c] == 'G' ){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method will get the all possible moves that the current grid is in, or the neighbors
     * @return it will return a hashset of all the possible moves on the current grid or configuration
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> neighbors = new HashSet<>();
        for(int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if(r % 2 == 0 && c % 2 == 0 && grid[r][c] != '*'){
                    //moving to the left
                    if(grid[r][c] == 'R' || grid[r][c] == 'G'){
                        if(c - 4 >= 0 ) {
                            if (grid[r][c - 2] == 'G' && grid[r][c - 4] == '.') {
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r][c - 4] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r][c - 2] = '.';
                                neighbors.add(gridCopy);

                            }
                        }
                        //moving to the right
                          if (c + 4 < columns ) {
                          if (grid[r][c + 2] == 'G' && grid[r][c + 4] == '.') {
                              HoppersConfig gridCopy = new HoppersConfig(this);
                              char[][] grid2 = gridCopy.getGrid();
                                grid2[r][c +4] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r][c + 2] = '.';
                                neighbors.add(gridCopy);

                            }
                        }
                        //moving up
                         if (r - 4 > rows) {
                            if (grid[r - 2][c] == 'G' && grid[r + 4][c] == '.') {
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r - 4][c] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r - 2 ][c] = '.';
                                neighbors.add(gridCopy);

                            }
                        }

                        //moving down
                          if ( r + 4 < rows ) {
                            if (grid[r + 2][c] == 'G' && grid[r + 4][c] == '.') {
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r + 4][c] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r + 2][c] = '.';
                                neighbors.add(gridCopy);

                            }
                        }
                        //diagonally top left
                         if(c - 2 >= 0 && r - 2 >= 0 ) {
                         if (grid[r - 1][c - 1] == 'G' && grid[r - 2][c - 2] == '.') {
                             HoppersConfig gridCopy = new HoppersConfig(this);
                             char[][] grid2 = gridCopy.getGrid();
                             grid2[r - 2][c - 2] = grid2[r][c];
                             grid2[r][c] = '.';
                             grid2[r - 1][c - 1] = '.';
                             neighbors.add(gridCopy);
                             }
                         }
                         //diagonally bottom right
                        if (c + 2 < columns && r + 2 < rows ) {
                            if(grid[r + 1][c + 1] == 'G' && grid[r + 2][c + 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r + 2][c + 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r + 1][c + 1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }
                        //diagonally top right
                         if (c + 2 < columns && r - 2 >= 0 ) {
                            if(grid[r - 1][c + 1] == 'G' && grid[r - 2][c + 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r - 2][c + 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r - 1][c + 1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }
                        //diagonally bottom left
                         if (c - 2 >= 0 && r + 2 < rows ) {
                            if(grid[r + 1][c- 1] == 'G' && grid[r + 2][c - 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r + 2][c - 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r + 1][c -1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }

                    }

                }

                if(r % 2 != 0 && c % 2 != 0 && grid[r][c] != '*') {
                    if (grid[r][c] == 'R' || grid[r][c] == 'G' ) {
                        //diagonally top left
                        if(c - 2 >= 0 && r - 2 >= 0) {
                            if (grid[r - 1][c - 1] == 'G' && grid[r - 2][c - 2] == '.') {
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r - 2][c - 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r - 1][c - 1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }
                        //diagonally bottom right *********Change*******
                        if (c + 2 < columns && r + 2 < rows ) {
                            if(grid[r + 1][c + 1] == 'G' && grid[r + 2][c + 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r + 2][c + 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r + 1][c + 1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }
                        //diagonally top right
                        if (c + 2 < columns && r - 2 >= 0 ) {
                            if(grid[r - 1][c + 1] == 'G' && grid[r - 2][c + 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r - 2][c + 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r - 1][c + 1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }
                        //diagonally bottom left
                        if (c - 2 >= 0 && r + 2 < rows) {
                            if(grid[r + 1][c- 1] == 'G' && grid[r + 2][c - 2] == '.'){
                                HoppersConfig gridCopy = new HoppersConfig(this);
                                char[][] grid2 = gridCopy.getGrid();
                                grid2[r + 2][c - 2] = grid2[r][c];
                                grid2[r][c] = '.';
                                grid2[r + 1][c -1] = '.';
                                neighbors.add(gridCopy);
                            }
                        }


                    }
                }
            }

        }
        return neighbors;
    }

    /**
     * This is the getter method that will get the grid of the configuration
     * @return it returns the grid
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * This is the getter method that will return the amount of rows in the grid
     * @return it returns the amount of rows in the grid
     */
    public int getRows(){
        return rows;
    }

    /**
     * This is the getter method that will return the amount of rows in the grid
     * @return it returns the amount of columns in the grid
     */
    public int getColumns() {
        return columns;
    }

    /**
     * This method compares two objects and helps to find any duplicate configurations
     * @param o the other object
     * @return it returns whether the objects are equal to each other
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoppersConfig that = (HoppersConfig) o;
        return rows == that.rows && columns == that.columns && Arrays.deepEquals(grid, that.grid);
    }

    /**
     * This creates the hashcode of the objects in the grid and will help us so that we won't have any duplicates
     * @return it returns the hash code of the objects
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    /**
     * This creates the board but as a string
     * @return it returns a string version of the board
     */
    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for(int r = 0; r < rows; r++){
            if(r != 0) {
                a.append("\n");
            }
            for(int c = 0; c < columns; c++){
                a.append(grid[r][c]).append(" ");
            }
        }
        return a.toString();
    }
}