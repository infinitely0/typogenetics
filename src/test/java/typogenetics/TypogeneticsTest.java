package typogenetics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

public class TypogeneticsTest {

	@BeforeAll
	public void beforeAll() {
	}

	@BeforeEach
	public void beforeEach() {
	}

	@Test
	public void baseTest() throws Exception {
		char[] letters = { 'A', 'C', 'G', 'T' };
		for (char letter : letters) {
			Base L = new Base(letter);
			assertEquals(letter, L.getLetter().toString().charAt(0));

			if (letter == 'A' || letter == 'G') {
				assertEquals(Base.Derivative.purine, L.getDerivative());
			} else {
				assertEquals(Base.Derivative.pyrimidine, L.getDerivative());
			}
		}
	}

	@Test
	public void strandTest() throws Exception {
		String bases = "ACGT";
		Strand strand = new Strand(bases);
		assertEquals("A", strand.get(0).toString());
		assertEquals("T", strand.get(bases.length() - 1).toString());

		bases = "ACGTA";
		strand = new Strand(bases);
		assertEquals("A", strand.get(bases.length() - 1).toString());
	}

	@Test
	public void aminoAcidTest() throws Exception {
		Base A = new Base('A');
		Base T = new Base('T');

		AminoAcid aa = new AminoAcid(A, T);
		assertEquals(aa.toString(), "AT");
	}

	@Test
	public void proteinTest() throws Exception {
		Strand strand = new Strand("CATTTTTTAC");

		Protein protein = Ribosome.translate(strand, Protein.class).get(0);

		AminoAcid CA = protein.get(0);
		assertEquals(AminoAcid.Command.mvr, CA.getCommand());
		assertEquals(AminoAcid.Direction.s, CA.getDirection());

		protein.add(new AminoAcid(new Base('A'), new Base('T')));

		AminoAcid AT = protein.get(protein.size() - 1);
		assertEquals("AT", AT.toString());

		assertEquals(AminoAcid.Command.swi, AT.getCommand());
		assertEquals(AminoAcid.Direction.r, AT.getDirection());
	}

	@Test
	public void multiProteinTest() throws Exception {
		Strand strand = new Strand("CAATAACAAT");
		List<Protein> list = Ribosome.translate(strand, Protein.class);

		assertEquals(2, list.size());

		strand = new Strand("CAATAACAATAATT");
		list = Ribosome.translate(strand, Protein.class);

		Protein p = new Protein();
		p.add(new AminoAcid(new Base('T'), new Base('T')));
		assertEquals(p.toString(), list.get(list.size() - 1).toString());
	}

	@Test
	public void hangingBaseTest() throws Exception {
		Strand strand = new Strand("CCAATTC");
		List<Protein> list = Ribosome.translate(strand, Protein.class);

		Protein p = new Protein();
		p.add(new AminoAcid(new Base('T'), new Base('T')));
		assertEquals(list.get(list.size() - 1).toString(), p.toString());

		strand = new Strand("CCTTG");
		p = Ribosome.translate(strand, Protein.class).get(0);

		assertEquals("[CC, TT]", p.toString());
	}

	@Test
	public void enzymeTest() throws Exception {
		Strand strand = new Strand("CACACACAAT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		assertEquals(enzyme.get(0).toString(),
				new AminoAcid(strand.get(0), strand.get(1)).toString());
	}

	@Test
	public void enzymeMoveRightTest() {
		Strand strand = new Strand("CGCACACACTCT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, G, C, A, C, A, C, A, C, T, C, T]", strands.get(0).toString());
		assertEquals("[G, T, G]", strands.get(1).toString());
	}

	@Test
	public void enzymeMoveLeftTest() {
		Strand strand = new Strand("CTCGCCCC");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);

		List<Strand> strands = enzyme.bind(strand);

		assertEquals("[C, T, C, G, C, C, C, C]", strands.get(0).toString());
		assertEquals("[G]", strands.get(1).toString());
	}

	@Test
	public void bindingRotationTest() throws Exception {
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
	public void bindingOrientationTest() {
		// This one tests rotation (as above) but with a different orientation
		// - these strands start with AT, which is a "right" amino acid, or TT
		// which is "left"
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

