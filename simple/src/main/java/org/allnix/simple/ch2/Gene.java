package org.allnix.simple.ch2;

import java.util.ArrayList;
import java.util.Comparator;

import org.allnix.simple.ch2.Gene.Nucleotide;

/**
 * @author ykyang@gmail.com
 *
 */
public class Gene {
	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch2.Gene runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch2.Gene runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
		//String geneStr = "ACGT";
		Gene gene = new Gene(geneStr);
		//gene.print();
		//System.out.println(gene.codons.size());
		Codon acg = new Codon("ACG");
        Codon gat = new Codon("GAT");
        System.out.println(gene.linearContains(acg)); // true
        System.out.println(gene.linearContains(gat)); // false
	}
	public enum Nucleotide {
		A, C, G, T
	}

	private ArrayList<Codon> codons = new ArrayList<>();
	public int codonCount() {
		return codons.size();
	}
	public void print() {
		for (Codon codon : codons) {
			System.out.println(codon.first + " " + codon.second + " " + codon.third);
		}
	}
	public Gene(String geneStr) {
		for (int i = 0; (i + 2) < geneStr.length(); i++) {
			codons.add(new Codon(geneStr.substring(i, i+3)));
		}
	}
	
	public boolean linearContains(Codon key) {
		for (Codon codon : codons) {
			if (codon.compareTo(key) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	static public class Codon implements Comparable<Codon> {
		final public Nucleotide first, second, third;
		static Comparator<Codon> comparator;
		static {
			comparator = Comparator.comparing((Codon c)->c.first)
					.thenComparing((Codon c)->c.second)
					.thenComparing((Codon c)->c.third);
		}
		public Codon(String codonStr) {
			first = Enum.valueOf(Nucleotide.class, codonStr.substring(0, 1));
			second = Enum.valueOf(Nucleotide.class, codonStr.substring(1, 2));
			third = Enum.valueOf(Nucleotide.class, codonStr.substring(2, 3));
		}
		
		@Override
		public int compareTo(Codon other) {
			//>> Can comparator be reused?  Yes, as shown.
			
//			return Comparator.comparing((Codon c)->c.first)
//			.thenComparing((Codon c)->c.second)
//			.thenComparing((Codon c)->c.third)
//			.compare(this, other);
			
			return comparator.compare(this, other);
			
		
		}

	}
}