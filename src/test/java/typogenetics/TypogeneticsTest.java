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

}

