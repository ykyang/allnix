package org.allnix.classic.ch1;

import java.util.Stack;

/**
 * https://livebook.manning.com/book/classic-computer-science-problems-in-java/chapter-1/v-3/101
 * 
 * @author ykyang@gmail.com
 *
 */
public class Hanoi {
	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch1.Hanoi runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch1.Hanoi runApp
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Hanoi hanoi = new Hanoi(12);
		
	    hanoi.solve();
	 
	    System.out.println(hanoi.towerA);
	    System.out.println(hanoi.towerB);
	    System.out.println(hanoi.towerC);
	}
	
	private final int NUM_DISCS;
	public final Stack<Integer> towerA = new Stack<>();
	public final Stack<Integer> towerB = new Stack<>();
	public final Stack<Integer> towerC = new Stack<>();
	
	public Hanoi(int numDiscs) {
		NUM_DISCS = numDiscs;
		for (int i = 1; i <= numDiscs; i++) {
			towerA.push(i);
		}
	}
	private void move(Stack<Integer> begin, Stack<Integer> end, Stack<Integer> temp, int n) {
		
	    if (n == 1) {
	        end.push(begin.pop());
	    } else {
	    	System.out.println("n = " + n);
	        move(begin, temp, end, n - 1);
	        move(begin, end, temp, 1);
	        move(temp, end, begin, n - 1);
	    }
	}
	
	public void solve() {
        move(towerA, towerC, towerB, NUM_DISCS);
    }
}
