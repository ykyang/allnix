package org.allnix.simple.ch3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * TODO: Print on a board
 * 
 * @author ykyang@gmail.com
 *
 */
public class QueensConstraint extends Constraint<Integer, Integer> {
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch3.QueensConstraint runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch3.QueensConstraint runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        //> Variable
        List<Integer> columns = List.of(1,2,3,4,5,6,7,8);
        //> Domain that is admissible values for each column
        List<Integer> rows = List.of(1,2,3,4,5,6,7,8);
        //> Admissible values for each column are the same
        Map<Integer, List<Integer>> domainDb = new HashMap<>();
        for (int col : columns) {
            domainDb.put(col, rows);
        }
        
        CSP<Integer,Integer> csp = new CSP<>(columns, domainDb);
        Constraint<Integer,Integer> constraint = new QueensConstraint(columns);
        csp.addConstraint(constraint);
        Map<Integer,Integer> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println(solution);
        }
        
        print(solution, columns.size());
    }
    
    /**
     * Print the solution on a board
     * 
     * @param solution
     * @param size
     */
    public static void print(Map<Integer,Integer> solution, int size) {
        char[][] board = new char[size][size];
        //List<String> board = new ArrayList<>();
        
        final char B = 'X';
        final char W = ' ';
        final List<Character> BW = List.of('X', ' ');
        CyclicIterator<Character> color = new CyclicIterator<>(BW);
        //char C = B; // current color 
        //> Populate empty board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = color.next(); //'X'; how to do cyclic?
            }
            color.next();
        }
        
        //> Assign queen
        for (Entry<Integer,Integer> entry : solution.entrySet()) {
            int row = entry.getKey();
            int col = entry.getValue();
            board[row-1][col-1] = 'Q';
        }
        
        //> Print from last row to first row
        for (int row = size-1; row >= 0; row--) {
            StringBuffer sb = new StringBuffer();
            for (int col = 0; col < size; col++) {
                sb.append(board[row][col]);
            }
            
            System.out.println(sb.toString());
        }
    }
    /**
     * Column indices where there is a queen
     * 
     * @param columns
     */
    public QueensConstraint(List<Integer> columns) {
        super(columns);
    }

    /**
     * @param assignment (column,row)
     */
    @Override
    public boolean satisfied(Map<Integer, Integer> assignment) {
        //> For each assignment checks columns with higher indices
        
        for (Entry<Integer,Integer> entry : assignment.entrySet()) {
            //> col and row of current queen
            int col = entry.getKey();
            int row = entry.getValue();
            
            //> check with queens to the right
            for (int col_right = col+1; col_right <= variables.size(); col_right++) {
                if (!assignment.containsKey(col_right)) {
                    continue;
                }
                
                int row_right = assignment.get(col_right);
                //> same row
                if (row == row_right) {
                    return false;
                }
                //> same diagonal
                if (Math.abs(col-col_right) == Math.abs(row-row_right)) {
                    return false;
                }
            }
        }
        
        return true;
    }

}
