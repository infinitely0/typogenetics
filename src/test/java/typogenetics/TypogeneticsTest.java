package typogenetics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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
		String bases = "CAATACGTTAACGCATCAGGCTAC";
		Strand strand = new Strand(bases);
		assertEquals(strand.get(0).toString(),
				new Base(bases.charAt(0)).toString());

		// TODO Test base string of odd length
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
		Strand strand = new Strand("CAATACGTTAACGCATCAGGCTAC");

		Protein protein = Ribosome.translate(strand, Protein.class);

		AminoAcid CA = protein.get(0);
		assertEquals(AminoAcid.Command.mvr, CA.getCommand());
		assertEquals(AminoAcid.Direction.s, CA.getDirection());

		protein.add(new AminoAcid(new Base('A'), new Base('T')));

		AminoAcid AT = protein.get(protein.size() - 1);
		assertEquals("AT", AT.toString());

		assertEquals(AminoAcid.Command.swi, AT.getCommand());
		assertEquals(AminoAcid.Direction.r, AT.getDirection());

		// TODO Test base string of odd length
	}

	@Test
	public void enzymeTest() throws Exception {
		Strand strand = new Strand("CACACACAAT");
		Enzyme enzyme = Ribosome.translate(strand, Enzyme.class);

		assertEquals(enzyme.get(0).toString(),
				new AminoAcid(strand.get(0), strand.get(1)).toString());
	}

	@Test
	public void bindingRotationTest() throws Exception {

		String[] strands =  { "CA", "CACC", "CAAT", "CACT", "CACTCT" };
		String[] bindings = { "A",  "A",    "G",    "C",    "T" };

		for (int i = 0; i < strands.length; i++) {
			Strand strand = new Strand(strands[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}
	}

	@Test
	public void bindingOrientationTest() {

		String[] rightStart = { "ATCACC", "ATCAAT", "ATCACT", "ATCACTCT" };
		String[] bindings =   { "C",      "A",      "T",      "G" };

		for (int i = 0; i < rightStart.length; i++) {
			Strand strand = new Strand(rightStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class);
			assertEquals(bindings[i], enzyme.getBindingBase().toString());
		}

		String[] leftStart= { "GTCACC", "GTCAAT", "GTCACT", "GTCACTCT" };
		String[] bindings2 = { "G",      "T",      "A",      "C" };

		for (int i = 0; i < leftStart.length; i++) {
			Strand strand = new Strand(leftStart[i]);
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class);
			assertEquals(bindings2[i], enzyme.getBindingBase().toString());
		}
	}

}

