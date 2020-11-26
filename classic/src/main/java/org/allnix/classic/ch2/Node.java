package org.allnix.classic.ch2;

public class Node<T> implements Comparable<Node<T>> {
	private final T state;
	private Node<T> parent; // could be null
	double cost;
	double heurisitc;

	/**
	 * For DFS and BFS
	 * 
	 * @param state
	 * @param parent
	 */
	public Node(T state, Node<T> parent) {
		this.state = state;
		this.parent = parent;
	}
	
	/**
	 * For A*
	 * 
	 * @param state
	 * @param parent
	 * @param cost
	 * @param heuristic
	 */
	public Node(T state, Node<T> parent, double cost, double heuristic) {
		this.state = state;
		this.parent = parent;
		this.cost = cost;
		this.heurisitc = heuristic;
	}
	
	@Override
	public int compareTo(Node<T> other) {
		Double mine = cost + heurisitc;
		Double others = other.cost + other.heurisitc;
		
		return mine.compareTo(others);
	}

	public T getState() {
		return state;
	}

	public Node<T> getParent() {
		return parent;
	}

	public double getCost() {
		return cost;
	}

	public double getHeurisitc() {
		return heurisitc;
	}
	
}
