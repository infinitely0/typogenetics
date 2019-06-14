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
		Strand strand = new Strand("CAGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, A, T, G, T]", strands.get(0).toString());
	}

	@Test
	public void moveLeftTest() {
		Strand strand = new Strand("GACCGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[G, A, C, T, A, C, G, T]", strands.get(0).toString());
	}

	@Test
	public void searchRightPyrimidineTest() {
		Strand strand = new Strand("CTTAGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, T, A, A, G, A]", strands.get(0).toString());
	}

	@Test
	public void searchRightPurineTest() {
		Strand strand = new Strand("CTTCGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, T, C, G, T, T]", strands.get(0).toString());
	}

	@Test
	public void searchLeftPyrimidineTest() {
		Strand strand = new Strand("CTTGGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, T, A, G, G, A]", strands.get(0).toString());
	}

	@Test
	public void searchLeftPurineTest() {
		Strand strand = new Strand("CTTTGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, T, T, G, A, A]", strands.get(0).toString());
	}

	@Test
	public void insertTest() throws Exception {
		// Insert A
		Strand strand = new Strand("GACT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[G, A, C, A, T]", strands.get(0).toString());

		// Insert C
		strand = new Strand("GCCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[G, C, C, T, C]", strands.get(0).toString());

		// Insert G
		strand = new Strand("CTGG");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[C, T, G, G, G]", strands.get(0).toString());

		// Insert T
		strand = new Strand("CTGTCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[C, T, T, G, T, C, T]", strands.get(0).toString());
	}

	@Test
	public void insertWithCopyOnTest() throws Exception {
		Strand strand = new Strand("CGGA");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, A, G, G, A]", strands.get(0).toString());
		assertEquals("[T, G]", strands.get(1).toString());
	}


	@Test
	public void insertMultipleWithCopyOnTest() throws Exception {
		Strand strand = new Strand("CGGAGCCG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, A, C, G, A, G, C, C, G]", strands.get(0).toString());
		assertEquals("[G, T, C]", strands.get(1).toString());
	}

	@Test
	public void deleteTest() {
		Strand strand = new Strand("CTAG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, A]", strands.get(0).toString());
	}

	@Test
	public void deleteWithCopyOnTest() {
		Strand strand = new Strand("CGAG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[G, A, G]", strands.get(0).toString());
	}

	@Test
	public void switchTest() {
		Strand strand = new Strand("ATGT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[A, T, G, T]", strands.get(1).toString());
		assertEquals("[T]", strands.get(0).toString());
	}

	@Test
	public void cutTest() {
		Strand strand = new Strand("GCAC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[A, C]", strands.get(0).toString());
		assertEquals("[G, C, C]", strands.get(1).toString());
	}

	@Test
	public void offsetTest() {
		Strand strand = new Strand("GAGC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand, 1);
		assertEquals("[G, A, G, A, C, C]", strands.get(0).toString());
	}

	@Test
	public void isolationTest() {
		Strand strand = new Strand("ATCG");
		strand.add(2, null);

		Enzyme enzyme = Ribosome.translate(
				new Strand("ATGA"), Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[A]", strands.get(0).toString());
		assertEquals("[A, T]", strands.get(1).toString());
		assertEquals("[C, G]", strands.get(2).toString());
	}

	@Test
	public void rotationTest() throws Exception {
		// Tests rotation aspect of ternary structure only (the following
		// strands all start with CA which is a "straight" amino acid
		String[] strands =  { "CA", "CACC", "CAAT", "CACT", "CACTCT" };
		// This is where the enzyme produced from the above should bind
		String[] bindings = { "A",  "A",    "G",    "C",    "T" };

		for (int i = 0; i < strands.length; i++) {
			Strand strand = new Strand(strands[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}
	}

	@Test
	public void orientationTest() {
		// This tests rotation (as above) but with a different orientation,
		// e.g. these strands start with AT, which is a "right" amino acid, or
		// TT which is "left"
		String[] rightStart = { "ATCACC", "ATCAAT", "TTCACT", "TTCACTCT" };
		String[] bindings =   { "C",      "A",      "A",      "C" };

		for (int i = 0; i < rightStart.length; i++) {
			Strand strand = new Strand(rightStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}

		String[] leftStart = { "GTCACC", "GTCAAT", "GTCACT", "GTCACTCT" };
		String[] bindings2 = { "G",      "T",      "A",      "C" };

		for (int i = 0; i < leftStart.length; i++) {
			Strand strand = new Strand(leftStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			assertEquals(bindings2[i], enzyme.getBindingBase().toString());
		}
	}

	@Test
	@Ignore
	public void bookTest() {
		// This is the example process given in the book
		// rpu-inc-cop-mvr-mvl-swi-lpu-ist
		Strand enzymeStrand = new Strand("TCGCCGCACCATTTGT");
		Enzyme enzyme = Ribosome.translate(enzymeStrand, Enzyme.class).get(0);
		System.out.println(enzyme.getCommands());
		enzyme.setOption("output", "true");

		Strand strand = new Strand("TAGATCCAGTCCATCGA");
		List<Strand> strands = enzyme.bind(strand, 1);

		System.out.println(strands);
		assertEquals("[A, T, G]", strands.get(1).toString());
	}

	@Ignore
	@Test
	public void nopeTest() {

	}

}
