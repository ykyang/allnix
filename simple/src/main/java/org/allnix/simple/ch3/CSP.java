package org.allnix.simple.ch3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Variable
 * @param <D> Domain
 */
public class CSP<V, D> {
    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints;

    public CSP(List<V> variables, Map<V, List<D>> domains) {
        this.variables = variables;
        this.domains = domains;

        // Initialize constraints & check domains
        constraints = new HashMap<>();
        for (V v : variables) {
            // variable with empty constraints
            constraints.put(v, new ArrayList<>());
            // variable must have domsins
            if (!domains.containsKey(v)) {
                throw new IllegalArgumentException("Every Variable must have a domain.");
            }
        }
    }

    public void addConstraint(Constraint<V, D> constraint) {
        for (V v : constraint.getVariables()) {
            if (variables.contains(v)) {
                constraints.get(v).add(constraint);
            } else {
                throw new IllegalArgumentException("Variable in constraint not in CSP");
            }
        }
    }

    public boolean consistent(V variable, Map<V, D> assignment) {
        // Check assignment against constraints
        for (Constraint<V, D> constraint : constraints.get(variable)) {
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
