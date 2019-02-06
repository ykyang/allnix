package org.allnix.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Range {
	static final private Logger logger = LoggerFactory.getLogger(Range.class);
	private Integer end;

	public Range(Integer end) {
		this.end = end;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Set<Integer> expandToSet(String text) {
		text = text.replace(":", " : ");
		text = text.replace(";", " : "); // avoid typo
		String[] tokens = StringUtils.split(text, ", ");
		
		Set<Integer> items = new TreeSet<>();
		
		int currentNumber = 1;
		int previousNumber = 1;
		boolean isInterval = false;
		for (String token : tokens) {
			if (":".equals(token)) {
				isInterval = true;
				continue;
			}

			if ("end".equals(token)) {
				currentNumber = end;
			} else {
				currentNumber = Integer.parseInt(token);
			}

			if (isInterval) {
				for (int number = previousNumber + 1; number <= currentNumber; number++) {
					items.add(number);
				}
				isInterval = false;
			} else {
				items.add(currentNumber);
			}

			previousNumber = currentNumber;
		}

		
		return items;
	}
	
	
	/**
	 * 1, 3, 7: end -> 1 3 7 8 9 10
	 * 
	 * @param text
	 * @return
	 */
	public int[] expand(String text) {
		// > split with ":" and keep ":"

		List<String> tokenList = new ArrayList<>();
		{
			String[] tokens = text.split("((?<=:)|(?=:))");
//		String[] tokens = StringUtils.split(text, ":");
			for (String token : tokens) {
				String[] subTokens = StringUtils.split(token, ", ");
				Arrays.stream(subTokens).forEach((t) -> {
					tokenList.add(t);
				});
			}

		}

		logger.info(tokenList.toString());

//		int[] items = new int[tokens.length];
		List<Integer> items = new ArrayList<>();

		int currentNumber = 1;
		int previousNumber = 1;
		boolean isInterval = false;
		for (String token : tokenList) {
//		for (int ind = 0; ind < tokens.length; ind++) {
//			String token = tokens[ind];
//			Integer number = null;
			if (":".equals(token)) {
				isInterval = true;
				continue;
			}

			if ("end".equals(token)) {
				currentNumber = end;
			} else {
				currentNumber = Integer.parseInt(token);
			}

			if (isInterval) {
				for (int number = previousNumber + 1; number <= currentNumber; number++) {
					items.add(number);
				}
				isInterval = false;
			} else {
				items.add(currentNumber);
			}

			previousNumber = currentNumber;
		}

		int[] ans = new int[items.size()];
		for (int i = 0; i < items.size(); i++) {
			ans[i] = items.get(i);
		}

		return ans;
	}
}
