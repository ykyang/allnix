package org.allnix.simple;

import java.util.BitSet;

/**
 * https://livebook.manning.com/book/classic-computer-science-problems-in-java/chapter-1/v-3/57
 * 
 * @author ykyang@gmail.com
 *
 */
public class CompressedGene {
	/**
	 * ./gradlew -PmainClass=org.allnix.simple.CompressedGene runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.CompressedGene runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		final String original = "TAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATATAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATA";
		CompressedGene compressed = new CompressedGene(original);
		final String decompressed = compressed.decompress();
		System.out.println(decompressed);
		System.out.println("original is the same as decompressed: " + original.equalsIgnoreCase(decompressed));
	}
	
	
	private BitSet bitSet;
	private int length;
	
	public CompressedGene(String gene) {
		compress(gene);
	}
	
	private void compress(String gene) {
		length = gene.length();
		bitSet = new BitSet(2*length);
		final String upperGene = gene.toUpperCase();
		for (int i = 0; i < length; i++) {
			final int ind = 2*i;
			boolean bit1, bit2;
			switch (upperGene.charAt(i)) {
			case 'A': // 00
				bit1 = false;
				bit2 = false;
				break;
			case 'C': // 01
				bit1 = false;
				bit2 = true;
				break;
			case 'G': // 10
				bit1 = true;
				bit2 = false;
				break;
			case 'T': // 11
				bit1 = true;
				bit2 = true;
				break;
			default:
				throw new IllegalArgumentException(
					"The provided gene String contains characters other than ACGT");		
			}
			
			bitSet.set(ind, bit1);
			bitSet.set(ind+1, bit2);
		}
	}
	
	private String decompress() {
		if (bitSet == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(length/2);
		for (int ind = 0; ind < (2*length); ind+=2) {
			// Book's version
//			final int firstBit = (bitSet.get(i) ? 1 : 0);
//	        final int secondBit = (bitSet.get(i + 1) ? 1 : 0);
//	        final int lastBits = firstBit << 1 | secondBit;
//	        switch (lastBits) {
//	        case 0b00: // 00 is 'A'
//	            builder.append('A');
//	            break;
//	        case 0b01: // 01 is 'C'
//	            builder.append('C');
//	            break;
//	        case 0b10: // 10 is 'G'
//	            builder.append('G');
//	            break;
//	        case 0b11: // 11 is 'T'
//	            builder.append('T');
//	            break;
//	        }
			final boolean bit1 = bitSet.get(ind);
			final boolean bit2 = bitSet.get(ind+1);
			if (bit1==false && bit2==false) {
				sb.append('A');
			} else if (bit1==false && bit2==true) {
				sb.append('C');
			} else if (bit1==true && bit2==false) {
				sb.append('G');
			} else {
				sb.append('T');
			}
		}
		
		return sb.toString();
		
	}
}
