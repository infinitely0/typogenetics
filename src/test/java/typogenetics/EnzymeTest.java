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

		assertEquals(enzyme.get(0).toString(),
				new AminoAcid(strand.get(0), strand.get(1)).toString());
	}

	@Test
	public void moveRightTest() {
		Strand strand = new Strand("CGCACACACTCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, C, A, C, A, C, A, C, T, C, T]", strands.get(0).toString());
		assertEquals("[G, T, G]", strands.get(1).toString());
	}

	@Test
	public void moveLeftTest() {
		Strand strand = new Strand("CTCGCCCC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, C, G, C, C, C, C]", strands.get(0).toString());
		assertEquals("[G]", strands.get(1).toString());
	}

	@Test
	public void searchRightTest() {
		// Search right pyrimidine
		Strand strand = new Strand("CGTACGCTCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[A, C]", strands.get(1).toString());

		// Search right purine
		strand = new Strand("CGTCCGCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[C, G, G]", strands.get(1).toString());
	}

	@Test
	public void searchLeftTest() {
		// Search left pyrimidine
		Strand strand = new Strand("CGTGCGCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G]", strands.get(1).toString());

		// Search left purine
		strand = new Strand("AACTCTCGTTCG");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[G, A, G, A, G, T]", strands.get(1).toString());
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
	public void insertCopyTest() throws Exception {
		// Insert A with copy on
		Strand strand = new Strand("CGGACG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, G, A, A, C, G]", strands.get(0).toString());
		assertEquals("[T]", strands.get(1).toString());
	}


	@Test
	public void insertCopyMultipleTest() throws Exception {
		// Insert A with copy on
		Strand strand = new Strand("CGGAGCCG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, A, C, G, A, G, C, C, G]", strands.get(0).toString());
		assertEquals("[G, T]", strands.get(1).toString());
	}


	@Test
	public void deleteTest() {
		Strand strand = new Strand("CGAGCG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, G, C, G]", strands.get(0).toString());
	}

	@Test
	public void deleteCopyTest() {
		Strand strand = new Strand("CGGAAGCG");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, G, A, A, G, C, G]", strands.get(0).toString());
	}

	@Test
	public void switchTest() {
		Strand strand = new Strand("ATCTGAGCCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[A, T, C, T, G, A, G, C, C, T]", strands.get(1).toString());
		assertEquals("[A, C]", strands.get(0).toString());
	}

	@Test
	@Ignore
	public void nopeTest() {
		Strand strand = new Strand("CTAGATCACCCGCTGAGCGGGTTATCTGTT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		System.out.println(enzyme.getCommands());

		List<Strand> strands = enzyme.bind(strand);

		System.out.print(strands);

		assertEquals(1, 0);
	}

	@Test
	public void isolationTest() {
		Strand strand = new Strand("ATCG");
		strand.add(2, null);

		Enzyme enzyme = Ribosome.translate(
				new Strand("ATGA"), Enzyme.class).get(0);
		enzyme.setOption("output", "true");

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

}
