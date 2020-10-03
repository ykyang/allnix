package org.allnix.simple.ch2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;

public class Search {
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
		
		return null;
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
