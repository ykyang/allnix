package org.allnix.classic.ch2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class Search {
	public static <T> Node<T> astar(T initial, Predicate<T> goalTest,
			Function<T, List<T>> successors, ToDoubleFunction<T> heuristic) {
		
		Node<T> node;
		PriorityQueue<Node<T>> frontier = new PriorityQueue<>(); // yet to go
		// state -> cost
		HashMap<T, Double> explored = new HashMap<>(); // visited
		
		node = new Node<T>(initial, null, 0.0, heuristic.applyAsDouble(initial));
		frontier.offer(node);

		while (!frontier.isEmpty()) {
			Node<T> currentNode = frontier.poll();
			T currentState = currentNode.getState();
			
			if (goalTest.test(currentState)) {
				return currentNode;
			}
			
			for (T child : successors.apply(currentState)) {
				double childCost = currentNode.getCost() + 1;
				
				// Not been there 
				// or
				// new child cost is less than existing one
				if (!explored.containsKey(child) || childCost < explored.get(child)) {
					explored.put(child, childCost);
					node = new Node<T>(child, currentNode, childCost, 
							heuristic.applyAsDouble(child));
					frontier.offer(node);
				}
			}
		}
		
		return null; // goal not found
	}
	
	/**
	 * Breadth-first search
	 * 
	 * @param <T>
	 * @param initial Initial state
	 * @param goalTest Test if the goal has reached
	 * @param successors Returns list of next state (node)
	 * @return Destination
	 */
	static public <T> Node<T> bfs(T initial, Predicate<T> goalTest,
			Function<T, List<T>> successors) {
		// Where we have yet to go
		Queue<Node<T>> frontier = new LinkedList<>();
		frontier.offer(new Node<>(initial, null));
		// Where we have been
		Set<T> explored = new HashSet<>();
		explored.add(initial);
		
		while(!frontier.isEmpty()) {
			Node<T> currentNode = frontier.poll();
			T currentState = currentNode.getState();
			
			if (goalTest.test(currentState)) {
				return currentNode;
			}
			
			for (T child : successors.apply(currentState)) {
				if (explored.contains(child)) {
					continue;
				}
				explored.add(child);
				frontier.offer(new Node<>(child, currentNode));
			}
		}
		
		return null; // goal not found
	}
	
	/**
	 * 
	 * boolean Predicate(T)
	 * List<T> Function(T)
	 * 
	 * @param <T>
	 * @param initial
	 * @param goalTest
	 * @param successors
	 * @return Goal node if reached otherwise null
	 */
	static public <T> Node<T> dfs(T initial, Predicate<T> goalTest,
			Function<T, List<T>> successors) {
		// Where we have yet to go
		Stack<Node<T>> frontier = new Stack<>();
		frontier.push(new Node<>(initial, null));
		
		// Where we have been
		Set<T> explored = new HashSet<>(); 
		explored.add(initial);
		
		
		while (!frontier.isEmpty()) {
			Node<T> currentNode = frontier.pop();
			T currentState = currentNode.getState();
			if (goalTest.test(currentState)) { // Predicate.test
				return currentNode;
			}
			
			// Not at the goal so check where to go next
			for (T child : successors.apply(currentState)) { // Function.apply
				if (explored.contains(child)) { // visted
					continue;
				}
				explored.add(child);
				frontier.push(new Node<>(child, currentNode));
			}
		}
		
		return null; // Cannot reach the goal
	}
	
	/**
	 * Turn give node to a list of nodes that leads to it.
	 * 
	 *  
	 * @param <T>
	 * @param node
	 * @return List of nodes from the start to the given node
	 */
	static public <T> Collection<T> nodeToPath(Node<T> node) {
		ArrayDeque<T> path = new ArrayDeque<>();
		path.push(node.getState());
		while (node.getParent() != null) {
			node = node.getParent();
			path.push(node.getState());
		}

		return path; 
	}
	
	
}
