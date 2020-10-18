package org.allnix.simple.ch3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constraint-Satisfaction Problem
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Variable
 * @param <D> Domain
 */
public class CSP<V, D> {
    /**
     * List of all variables
     */
    private List<V> variables;
    /**
     * Mapping of variable to its admissible values
     */
    private Map<V, List<D>> domains;
    /**
     * Mapping of variable to its constraints
     */
    private Map<V, List<Constraint<V, D>>> constraintDb;

    public CSP(List<V> variables, Map<V, List<D>> domainDb) {
        this.variables = variables;
        this.domains = domainDb;

        //> The following can be done in one-loop but
        //> it is more clear to be separated.
        
        //> Check all variables have domain
        for (V v : variables) {
            if (domainDb.containsKey(v)) {
                continue;
            }
            
            throw new IllegalArgumentException("Every Variable must have a domain.");
        }

        //> Initialize constraintDb
        constraintDb = new HashMap<>();
        for (V v : variables) {
            //> Empty list of constraints for each variable
            constraintDb.put(v, new ArrayList<>());
        }
    }

    /**
     * Add a new constraint between variables
     * 
     * Convert 
     * constraint -> variable mapping 
     * to 
     * variable -> constraint mapping 
     * as shown below
     * C1{V1, V2...} => V1{C1}, V2{C1}
     * @param constraint
     */
    public void addConstraint(Constraint<V, D> constraint) {
        for (V v : constraint.getVariables()) {
            if (variables.contains(v)) {
                constraintDb.get(v).add(constraint);
            } else {
                throw new IllegalArgumentException("Variable in constraint not in CSP");
            }
        }
    }

    public boolean consistent(V variable, Map<V, D> assignment) {
        // Check assignment against constraints
        for (Constraint<V, D> constraint : constraintDb.get(variable)) {
            if (!constraint.satisfied(assignment)) {
                return false;
            }
        }

        return true;
    }

    public Map<V, D> backtrackingSearch(Map<V, D> assignment) {
        // Every variable is assigned so stop
        if (assignment.size() == variables.size()) {
            return assignment;
        }

        // Get the first variable that is not in assignment
        V notassigned = variables.stream().filter(v -> {
            return !assignment.containsKey(v);
        }).findFirst().get();
        
        // Check all possible domain value for the not assigned variable
        for (D value : domains.get(notassigned)) {
            Map<V,D> nextAssignment = new HashMap<>(assignment);
            nextAssignment.put(notassigned, value);
            
            // Check if this variable is consistent
            if (consistent(notassigned, nextAssignment)) {
                Map<V,D> result = backtrackingSearch(nextAssignment);
                if (result != null) {
                    return result;
                }
            } else {
                // not consistent so move on to next domain value
            }
        }
        
        // no domain value make this not assigned variable consistent
        // backtrack to last variable's next domain value
        return null;
    }
    
    public Map<V,D> backtrackingSearch() {
        return backtrackingSearch(new HashMap<>());
    }
}
