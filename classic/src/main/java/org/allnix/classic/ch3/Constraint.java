package org.allnix.classic.ch3;

import java.util.List;
import java.util.Map;

/**
 * Constraint between variables from CSP: 3.1
 * 
 * Holds a list of variables and provide a method to 
 * check if the constraint is satisfied between the variables for 
 * an assignment.
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Variable type
 * @param <D> Domain (value) type
 */
public abstract class Constraint<V,D> {

	/**
	 * Constraint between the list of variables
	 */
	protected List<V> variables;
	
	public Constraint(List<V> variables) {
		this.variables = variables;
	}
	
	/**
	 * Checks if the assignment satisfies the constraint between
	 * the variables.
	 * 
	 * @param assignment Variables and their values (domain)
	 * @return true if constrain is satisfied
	 */
	public abstract boolean satisfied(Map<V,D> assignment);
	
	public List<V> getVariables() {
		return variables;
	}
}
