package org.allnix.classic.ch3;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class WordSearchConstraint extends Constraint<String, GridLocation[]>{
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch3.WordSearchConstraint runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch3.WordSearchConstraint runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        PrintStream out = System.out;
        
        WordGrid grid = new WordGrid(9, 9);
        List<String> words = List.of(
                "MATTHEW", "JOE", "MARY", "SARAH", "SALLY"
                );
        Map<String, List<GridLocation[]>> domainDb = new HashMap<>();
        for (String word : words) {
            domainDb.put(word, grid.generateDomain(word));
        }
        
        CSP<String,GridLocation[]> csp = new CSP<>(words, domainDb);
        csp.addConstraint(new WordSearchConstraint(words));
        Map<String, GridLocation[]> solution = csp.backtrackingSearch();
        if (solution == null) {
            out.println("No solution found!");
        } else {
            Random random = new Random();
            for (Entry<String,GridLocation[]> item : solution.entrySet()) {
                String word = item.getKey();
                GridLocation[] locations = item.getValue();
                
                // Reverse the word
//                if (random.nextBoolean()) {
//                    Arrays.reverse
//                }
                grid.mark(word, locations);
            }
            out.println(grid);
        }
    }
    public WordSearchConstraint(List<String> variables) {
        super(variables);
    }

    @Override
    public boolean satisfied(Map<String, GridLocation[]> assignment) {
//        Arrays.stream(assignment.values());
        List<GridLocation> allLocations = assignment
                .values() // Collection<GridLocation[]>
                .stream() // GridLocation[], GridLocation[] ...
                .flatMap(Arrays::stream) // stream(GridLocation[]) + stream(GridLocation[]) ...  
                .collect(Collectors.toList());
        
        Set<GridLocation> allLocationSet = new HashSet<>(allLocations);
        
        return allLocations.size() == allLocationSet.size();
    }

}
