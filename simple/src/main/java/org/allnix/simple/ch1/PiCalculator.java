package org.allnix.simple.ch1;

/**
 * https://livebook.manning.com/book/classic-computer-science-problems-in-java/chapter-1/v-3/91
 * 
 * @author ykyang@gmail.com
 *
 */
public class PiCalculator {

	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch1.PiCalculator runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch1.PiCalculator runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		System.out.println(calculatePi(1000_000));
	}
	static public double calculatePi(int nTerms) {
		double pi = 0.0;
		
		final double numerator = 4.0;
		double denominator = 1.0;
		double operation = 1.0;
		
		for (int i = 0; i < nTerms; i++) {
			pi += operation * numerator / denominator;
			denominator += 2.0;
			operation = -operation;
		}
		
		return pi;
	}
}
