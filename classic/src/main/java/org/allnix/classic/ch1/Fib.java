package org.allnix.classic.ch1;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 1.1 The Fibonacci sequence
 * 
 * 
 * https://www.mathsisfun.com/numbers/fibonacci-sequence.html
 * n  = 	0 	1 	2 	3 	4 	5 	6 	7 	8 	9 	10 	11 	12 	13 	14 	...
 * xn = 	0 	1 	1 	2 	3 	5 	8 	13 	21 	34 	55 	89 	144 233	377	...
 * 
 * 
 * @author ykyang@gmail.com
 *
 */
public class Fib {

	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch1.Fib runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch1.Fib runApp
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int x;
//		x = fib2(10);
//		System.out.println(x); // 55
		
//		x = fib3(0);
//		System.out.println(x); // 102334155
//		x = fib3(1);
//		System.out.println(x); // 55
//		x = fib3(2);
//		System.out.println(x); // 102334155
		
//		x = fib4(0);
//		System.out.println(x); // 0
//		x = fib4(1);
//		System.out.println(x); // 1
//		x = fib4(2);
//		System.out.println(x); // 1
//		x = fib4(10);
//		System.out.println(x); // 55
		
		// Fib5 example
		Fib fib = new Fib();
		fib.stream().limit(1+10).forEachOrdered(System.out::println);
	}
	
	static private Map<Integer,Integer> memo = new HashMap<>(Map.of(0,0,1,1)); 
	
	/**
	 * This method will go into infinite loop.
	 * 
	 * @param n
	 */
	static public int fib1(int n) {
		return fib1(n - 1) + fib1(n - 2);
	}
	static public int fib2(int n) {
		if (n < 2) {
			return n;
		}
		
		return fib2(n - 1) + fib2(n - 2);
	}
	static public int fib3(int n) {
		if (memo.containsKey(n)) {
			return memo.get(n);
		}
		
		int value = fib3(n-1) + fib3(n-2);
		memo.put(n, value);
		
		return value;
	}
	static public int fib4(int n) {
//        int last = 0, next = 1; // fib(0), fib(1)
//        for (int i = 0; i < n; i++) {
//            int oldLast = last;
//            last = next;
//            next = oldLast + next;
//        }
//        return last;
        
		int vn_2 = 0; // fib(n-2)
		int vn_1 = 1; // fib(n-1)
		int vn = n;   // fib(n)
		for (int i = 2; i <= n; i++) { // from 2 to n
			vn = vn_2 + vn_1;
			// move forward
			vn_2 = vn_1;
			vn_1 = vn;
		}
				
        return vn;
    }
	
	private int vn_2 = 0;
	private int vn_1 = 1;
	private int vn_c = 0;
	/**
	 * Use limit(1+n) to calculate upto fib(n)
	 * 
	 * Seems to me stream() is not a good way.  To calculate n, (n+1) needs
	 * to be passed.
	 * 
	 * @return 
	 */
	public IntStream stream() {
		return IntStream.generate(() -> {
			int ans = vn_c;

			//vn_c = vn_1 + vn_2;
			vn_2 = vn_1;
			vn_1 = vn_c;
			vn_c = vn_1 + vn_2;
			
			return ans;
		});
	}
}
