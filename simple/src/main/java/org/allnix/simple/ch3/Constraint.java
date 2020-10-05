package org.allnix.simple.ch3;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Variable type
 * @param <D> Domain type
 */
public abstract class Constraint<V,D> {

	/**
	 * Constraint between the list of variables
	 */
	protected List<V> variables;
	
	public Constraint(List<V> variables) {
		this.variables = variables;
	}
	
	public abstract boolean satisfied(Map<V,D> assignment);
	
	public List<V> getVariables() {
		return variables;
	}
}
