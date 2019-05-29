package typogenetics;

import java.util.List;
import java.util.ArrayList;

public class Strand {

	private List<Base> sequence;

	public Strand(List<Base> sequence) {
		this.sequence = sequence;
	}

	public Strand(String sequence) throws InvalidBaseException {
		this.sequence = parseStrand(sequence);
	}

	public List<Base> getSequence() {
		return sequence;
	}

	public static List<Base> parseStrand(String string) throws InvalidBaseException {
		List<Base> sequence = new ArrayList<>();

		for (int i = 0; i < string.length(); i++) {
			char letter = string.charAt(i);
			Base base = new Base(letter);
			sequence.add(base);
		}

		return sequence;
	}

}

