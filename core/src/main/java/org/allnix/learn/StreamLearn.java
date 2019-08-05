package org.allnix.learn;

import java.util.stream.Stream;

public class StreamLearn {

	static public void main(String[] args) {
		Stream.empty();
		
		Stream.<String>builder().add("a").build();
		
		Stream.generate(() -> "element").limit(10);
		Stream.iterate(2, n -> n+2).limit(10);
	}
}
