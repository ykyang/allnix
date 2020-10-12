package org.allnix.simple.ch3;

import java.util.List;
import java.util.Random;

/**
 * CSP: 3.4
 * 
 * @author ykyang@gmail.com
 *
 */
public class WordGrid {
    private final int ALPHABET_LENGTH = 26;
    private final char FIRST_LETTER = 'A';
    private final int rows, columns;
    private char[][] grid;
    
    public WordGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        
        grid = new char[rows][columns];
        
        Random random = new Random();
        
        //> Initialize the grid with random letters
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                char letter = (char) (random.nextInt(ALPHABET_LENGTH) + FIRST_LETTER);
                grid[row][col] = letter;
            }
        }
    }
    
    public void mark(String word, List<GridLocation> locations) {
        for (int i = 0; i < word.length(); i++) {
            GridLocation loc = locations.get(i);
            grid[loc.row][loc.column] = word.charAt(i);
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(row);
            sb.append(System.lineSeparator());
        }
        
        return sb.toString();
    }
    
}
