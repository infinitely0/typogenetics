package typogenetics;

import java.util.List;
import java.util.ArrayList;
import java.util.AbstractList;

public class Strand extends AbstractList<Base> {

	private final List<Base> sequence;

	public Strand(String sequence) {
		this.sequence = parseStrand(sequence);
	}

	@Override
	public Base get(int index) {
		return sequence.get(index);
	}

	@Override
	public int size() {
		return sequence.size();
	}

	public static List<Base> parseStrand(String string) {
		List<Base> sequence = new ArrayList<>();

		for (int i = 0; i < string.length(); i++) {
			char letter = string.charAt(i);
			Base base = new Base(letter);
			sequence.add(base);
		}

		return sequence;
	}

}

