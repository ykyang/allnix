package org.allnix.classic.ch3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSP: 3.2
 * 
 * @author ykyang@gmail.com
 *
 */
public class MapColoringConstraint extends Constraint<String,String> {
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch3.MapColoringConstraint runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch3.MapColoringConstraint runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        final String WA = "Western Australia";
        final String NT = "Northern Territory";
        final String SA = "South Australia";
        final String QU  = "Queensland";
        final String NS = "New South Wales";
        final String VI  = "Victoria";
        final String TA  = "Tasmania";
        
        
        //> Variable is state (using US terminology for simplicity)
        List<String> states = List.of(WA, NT, SA, QU, NS, VI, TA);
        
        //> All variables have the same admissible domain, that is
        //> all states have the same admissible colors.
        List<String> colorList = List.of("red", "green", "blue");
        Map<String,List<String>> domainDb = new HashMap<>();
        for (String state : states) {
            domainDb.put(state, colorList);
        }
        
        CSP<String,String> csp = new CSP<>(states, domainDb);
        
        //> Binary constraints
        Constraint<String,String> constraint;
        
        constraint = new MapColoringConstraint(WA, NT);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(WA, SA);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(SA, NT);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(QU, NT);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(QU, SA);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(QU, NS);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(NS, SA);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(VI, SA);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(VI, NS);
        csp.addConstraint(constraint);
        
        constraint = new MapColoringConstraint(VI, TA);
        csp.addConstraint(constraint);
        
        Map<String,String> solution = csp.backtrackingSearch();
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println(solution);
        }
            
    }
//    private String place1;
//    private String place2;
    
    public MapColoringConstraint(String p1, String p2) {
        super(List.of(p1,p2));
        
//        this.place1 = p1;
//        this.place2 = p2;
    }
    
    @Override
    public boolean satisfied(Map<String, String> assignment) {
        //> If any place is not in the assignment, then
        //> it is not conflicting.
        for (String place : variables) {
            if (!assignment.containsKey(place)) {
                return true;
            }
        }
        
        String value1 = assignment.get(variables.get(0));
        String value2 = assignment.get(variables.get(1));
                      
        // check value1 for null?
        return !value1.equals(value2);
    }
    
    
    
}
