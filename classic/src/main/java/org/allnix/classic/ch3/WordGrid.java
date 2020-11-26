package org.allnix.classic.ch3;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CSP: 3.4
 * 
 * @author ykyang@gmail.com
 *
 */
public class WordGrid {
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch3.WordGrid runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch3.WordGrid runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        PrintStream out = System.out;
        
        GridLocation[] l = new GridLocation[10];
        l[0] = new GridLocation(1,2);
        out.println(l);
    }
    
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
    
    public void mark(String word, GridLocation[] locations) {
        for (int i = 0; i < word.length(); i++) {
            GridLocation loc = locations[i];
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
    
    public List<GridLocation[]> generateDomain(String word) {
       List<GridLocation[]> domain = new ArrayList<>();
       int length = word.length();
       
       // Get all the possible locations
       for (int row = 0; row < rows; row++) {
           for (int col = 0; col < columns; col++) {
               if (col + length <= columns) {
                   domain.add(fill90(row,col,length));
                   if (row + length <= rows) {
                       domain.add(fill135(row,col,length));
                   }
               }
               if (row + length <= rows) {
                   domain.add(fill180(row,col,length));
                   if (col - length >= 0) {
                       domain.add(fill225(row,col,length));
                   }
               }
           }
       }
       
       return domain;
    }
    /**
     * Fill word to the right (90 degree)
     * 
     * @param row
     * @param column
     * @param length
     * @return
     */
    private GridLocation[] fill90(int row, int column, int length) {
        GridLocation[] gls = new GridLocation[length];
                
        for (int c = column, ind = 0; c < (column+length); c++, ind++) {
            gls[ind] = new GridLocation(row,c);
        }
        
        return gls;
    }
    
    private GridLocation[] fill135(int row, int column, int length) {
        GridLocation[] gls = new GridLocation[length];
        
        for (int c = column, r=row, ind = 0; 
                c < (column+length); 
                c++, r++, ind++) {
            gls[ind] = new GridLocation(r,c);
        }
        
        return gls;
    }
    
    private GridLocation[] fill180(int row, int column, int length) {
        GridLocation[] gls = new GridLocation[length];
        
        for (int r = row, ind = 0; r < (row + length); r++, ind++) {
            gls[ind] = new GridLocation(r,column);
        }
        
        return gls;
    }
    
    private GridLocation[] fill225(int row, int column, int length) {
        GridLocation[] gls = new GridLocation[length];
        
        int c = column;
        int ind = 0;
        for (int r = row; r < (row+length); r++) {
            gls[ind] = new GridLocation(r,c);
            --c;
            ++ind;
        }
        
        return gls;
    }
}
