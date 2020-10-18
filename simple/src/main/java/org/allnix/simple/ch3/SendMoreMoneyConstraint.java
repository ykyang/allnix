package org.allnix.simple.ch3;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SendMoreMoneyConstraint extends Constraint<Character, Integer> {
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch3.SendMoreMoneyConstraint runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch3.SendMoreMoneyConstraint runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        PrintStream out = System.out;
        
        List<Character> letters = List.of('S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y');
        Map<Character,List<Integer>> domainDb = new HashMap<>();
        List<Integer> domain = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        //> Every char can be 0-9
        for (Character letter : letters) {
            domainDb.put(letter, domain);
        }
        // so we don't get answers starting with a 0
        domainDb.replace('M', List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        CSP<Character, Integer> csp = new CSP<>(letters, domainDb);
        Constraint<Character,Integer> constraint = new SendMoreMoneyConstraint(letters);
        csp.addConstraint(constraint);
        Map<Character,Integer> solution = csp.backtrackingSearch();
        if (solution == null) {
            out.println("No solution found!");
        } else {
            out.println(solution);
            int s = solution.get('S');
            int e = solution.get('E');
            int n = solution.get('N');
            int d = solution.get('D');
            int m = solution.get('M');
            int o = solution.get('O');
            int r = solution.get('R');
//            int e = solution.get('E');
//            int m = solution.get('M');
//            int o = solution.get('O');
//            int n = solution.get('N');
//            int e = solution.get('E');
            int y = solution.get('Y');
//            String.format("%d", s);
            String formula = String.format("%d%d%d%d + %d%d%d%d = %d%d%d%d%d", s,e,n,d,m,o,r,e,m,o,n,e,y);
            out.println(formula);
        }
        
        
    }
    
    private List<Character> letters;
    
    public SendMoreMoneyConstraint(List<Character> letters) {
        super(letters);
        this.letters = letters;
    }

    @Override
    public boolean satisfied(Map<Character, Integer> assignment) {
        //> if (same number assigned to different char) -> not a solution
        HashSet<Integer> uniqueNumber = new HashSet<>(assignment.values());
        if (assignment.size() != uniqueNumber.size()) {
            return false;
        }
        
        //> All letters have unique number
        
        //> Not all letters are assigned so it is satisfied so far
        if (assignment.size() < letters.size()) {
            return true;
        }
        
        if (assignment.size() > letters.size()) {
            throw new IllegalStateException("This should never happen");
        }
        
        //> assignment.size() == letters.size()
        int s = assignment.get('S');
        int e = assignment.get('E');
        int n = assignment.get('N');
        int d = assignment.get('D');
        int m = assignment.get('M');
        int o = assignment.get('O');
        int r = assignment.get('R');
        int y = assignment.get('Y');
        int send = 1000*s + 100*e + 10*n + d;
        int more = 1000*m + 100*o + 10*r + e;
        int money = 10000*m + 1000*o + 100*n + 10*e + y;
        
        return money == (send + more);
    }
}
