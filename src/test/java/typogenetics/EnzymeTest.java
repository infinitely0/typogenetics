package typogenetics;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import java.util.List;

public class EnzymeTest {

	@Test
	public void initTest() throws Exception {
		Strand strand = new Strand("CACACACAAT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		assertEquals(enzyme.get(0).toString(),
				new AminoAcid(strand.get(0), strand.get(1)).toString());
	}

	@Test
	@Ignore
	public void moveRightTest() {
		Strand strand = new Strand("CGCACACACTCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, C, A, C, A, C, A, C, T, C, T]", strands.get(0).toString());
		assertEquals("[G, T, G]", strands.get(1).toString());
	}

	@Test
	@Ignore
	public void moveLeftTest() {
		Strand strand = new Strand("CTCGCCCC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, C, G, C, C, C, C]", strands.get(0).toString());
		assertEquals("[G]", strands.get(1).toString());
	}

	@Test
	@Ignore
	public void searchRightTest() {
		// Search right pyrimidine
		Strand strand = new Strand("CGTACGCTCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, A]", strands.get(1).toString());

		// Search right purine
		strand = new Strand("CGTCCGCT");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[G, G, C]", strands.get(1).toString());
	}

	@Test
	@Ignore
	public void searchLeftTest() {
		// Search left pyrimidine
		Strand strand = new Strand("CGTGCGCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[G, C]", strands.get(1).toString());

		// Search left purine
		strand = new Strand("AACTCTCGTTCG");
		enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
		strands = enzyme.bind(strand);

		assertEquals("[T, G, A, G, A, G]", strands.get(1).toString());
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
