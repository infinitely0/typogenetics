package typogenetics;

import java.util.List;
import java.util.ArrayList;

public class Typogenetics {

	public static void main(String[] args) throws InvalidBaseException {

		Strand strand = new Strand("ATACGTTAACGCATCAGGCTAC");
		List<Base> sequence = strand.getSequence();

		List<AminoAcid> aminoAcids = new ArrayList<>();

		for (int i = 0; i < sequence.size(); i++) {
			Base first = sequence.get(i);
			Base second = sequence.get(++i);

			AminoAcid aminoAcid = new AminoAcid(first, second);
			aminoAcids.add(aminoAcid);
		}

		System.out.println(aminoAcids);

		AminoAcid AT = new AminoAcid(new Base('A'), new Base('T'));
		System.out.println(AT.getCommand());
	}

}

