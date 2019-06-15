package typogenetics;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class EnzymeTest {

	@Test
	public void initTest() throws Exception {
		Strand strand = new Strand("CACACACAAT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		assertEquals(enzyme.get(0).toString(), "CA");
	}

	@Test
	public void moveRightTest() {
		Strand strand = new Strand("CAGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CAAGA", strands.get(0).toString());
	}

	@Test
	public void moveLeftTest() {
		Strand strand = new Strand("CCGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CCGTT", strands.get(0).toString());
	}

	@Test
	public void searchRightPyrimidineTest() {
		Strand strand = new Strand("CTTAGG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CTTGAGG", strands.get(0).toString());
	}

	@Test
	public void searchRightPurineTest() {
		Strand strand = new Strand("CTTCGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CTTCGTT", strands.get(0).toString());
	}

	@Test
	public void searchLeftPyrimidineTest() {
		Strand strand = new Strand("TGGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("TAGGA", strands.get(0).toString());
	}

	@Test
	public void searchLeftPurineTest() {
		Strand strand = new Strand("CTTTGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CTTTGA", strands.get(0).toString());
	}

	@Test
	public void insertTest() throws Exception {
		// Insert A
		Strand strand = new Strand("GACT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("GACTA", strands.get(0).toString());

		// Insert C
		strand = new Strand("GCCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("GCCCT", strands.get(0).toString());

		// Insert G
		strand = new Strand("CTGG");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("CTGGG", strands.get(0).toString());

		// Insert T
		strand = new Strand("CTGTCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("CTTGTCT", strands.get(0).toString());
	}

	@Test
	public void insertWithCopyOnTest() throws Exception {
		Strand strand = new Strand("CGGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CGTGT", strands.get(0).toString());
		assertEquals("AC", strands.get(1).toString());
	}


	@Test
	public void insertMultipleWithCopyOnTest() throws Exception {
		Strand strand = new Strand("CGGAGCCG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CGGAACGCCG", strands.get(0).toString());
		assertEquals("GTT", strands.get(1).toString());
	}

	@Test
	public void deleteTest() {
		Strand strand = new Strand("CTAG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("CTG", strands.get(0).toString());
	}

	@Test
	public void deleteWithCopyOnTest() {
		Strand strand = new Strand("CGCGAG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("GCGAG", strands.get(0).toString());
	}

	@Test
	public void switchTest() {
		Strand strand = new Strand("ATGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("ATGT", strands.get(1).toString());
		assertEquals("T", strands.get(0).toString());
	}

	@Test
	public void cutTest() {
		Strand strand = new Strand("GCGCAC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("GCAC", strands.get(0).toString());
		assertEquals("GCCC", strands.get(1).toString());
	}

	@Test
	public void offsetTest() {
		Strand strand = new Strand("GCGC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand, 1);
		assertEquals("GCGCCC", strands.get(0).toString());
	}

	@Test
	public void isolationTest() {
		Strand strand = new Strand("ATCG");
		strand.add(2, null);

		Strand enzymeStrand = new Strand("ATGC");
		Enzyme enzyme = Ribosome.translate(enzymeStrand, Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand);
		assertEquals("C", strands.get(0).toString());
		assertEquals("AT", strands.get(1).toString());
		assertEquals("CG", strands.get(2).toString());
	}

	@Test
	public void rotationTest() throws Exception {
		// Tests rotation aspect of tertiary structure only (the following
		// strands all start with CA which is a "straight" amino acid
		String[] strands =  { "CA", "CACC", "CAAT", "CACT", "CACTCT" };
		// This is where the enzyme produced from the above should bind
		String[] bindings = { "C",  "C",    "A",    "T",    "G" };

		for (int i = 0; i < strands.length; i++) {
			Strand strand = new Strand(strands[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}
	}

	@Test
	public void orientationTest() {
		// This tests rotation (as above) but with a different orientation,
		// e.g. these strands start with AT which is a "right" amino acid, or
		// TT which is "left"
		String[] rightStart = { "ATCACC", "ATCAAT", "TTCACT", "TTCACTCT" };
		String[] bindings =   { "T",      "C",      "C",      "T" };

		for (int i = 0; i < rightStart.length; i++) {
			Strand strand = new Strand(rightStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}

		String[] leftStart = { "GTCACC", "GTCAAT", "GTCACT", "GTCACTCT" };
		String[] bindings2 = { "A",      "G",      "C",      "T" };

		for (int i = 0; i < leftStart.length; i++) {
			Strand strand = new Strand(leftStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings2[i], enzyme.getBindingBase().toString());
		}
	}

	@Test
	public void bookTest() {
		// This is the example process given in the book
		// rpu-inc-cop-mvr-mvl-swi-lpu-ist
		Strand enzymeStrand = new Strand("TCGCCGCACCATTTGT");
		Enzyme enzyme = Ribosome.translate(enzymeStrand, Enzyme.class).get(0);

		assertEquals("G", enzyme.getBindingBase().toString());

		Strand strand = new Strand("TAGATCCAGTCCATCGA");
		List<Strand> strands = enzyme.bind(strand, 1);

		assertEquals("ATG", strands.get(0).toString());
		assertEquals(
				"TAGATCCAGTCCACATCGA",
				strands.get(1).toString());
	}

	@Test
	@Ignore
	public void docTest() {
	  Strand strand = new Strand("CGGATACTGC");

	  // A ribosome may translate multiple enzymes from a single strand
	  List<Enzyme> enzymes = Ribosome.translate(strand, Enzyme.class);
	  Enzyme enzyme = enzymes.get(0);

	  // Print the steps between amino acid commands
	  enzyme.setOption("output", "true");

	  // The second parameter of bind() selects the index of the binding base
	  // (as determined by the its tertiary structure) i.e. 0 binds to the
	  // first G, 1 binds to second G etc.
	  List<Strand> products = enzyme.bind(strand, 0);

	  // From the list of strands, you can create more enzymes and bind them to
	  // any other of the resulting strands in any order
	  Strand one = products.get(0);
	  Strand two = products.get(1);
	  enzyme = Ribosome.translate(two, Enzyme.class).get(0);
	  products = enzyme.bind(one, 0);

	  if (products.get(0).equals(strand)) {
		  // Self-rep
	  }
	  assertEquals(1, 0);
	}

}

