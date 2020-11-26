package org.allnix.classic.ch2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.allnix.classic.ch2.Gene;
import org.allnix.classic.ch2.Gene.Codon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * ./gradlew test --tests GeneTest
 * gradlew.bat test --tests GeneTest
 * 
 * @author ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class GeneTest {
	static final String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
	
	/**
	 * 57 nucleotide results in 55 codons.
	 */
	@Test
	public void codonCount() {
		Gene gene = new Gene(geneStr);
		assertEquals(55, gene.codonCount());
	}
	
	/**
	 * Search given codon in a gene
	 */
	@Test
	public void linearContain() {
		Gene gene = new Gene(geneStr);

		Codon acg = new Codon("ACG");
        assertEquals(true, gene.linearContains(acg));
        
        Codon gat = new Codon("GAT");
        assertEquals(false, gene.linearContains(gat));
	}
	
	/**
	 * Search given codon in a gene
	 */
	@Test
	public void binaryContain() {
		Gene gene = new Gene(geneStr);

		Codon acg = new Codon("ACG");
        assertEquals(true, gene.binaryContains(acg));
        
        Codon gat = new Codon("GAT");
        assertEquals(false, gene.binaryContains(gat));
	}
	
}
