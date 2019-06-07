package typogenetics;

import java.util.List;
import java.util.ArrayList;
import java.util.AbstractList;

public class Strand extends AbstractList<Base> {

	private final List<Base> sequence;

	public Strand() {
		sequence = new ArrayList<>();
	}

	public Strand(String sequence) {
		this.sequence = parseStrand(sequence);
	}

	@Override
	public Base get(int index) {
		return sequence.get(index);
	}

	@Override
	public boolean add(Base base) {
		return sequence.add(base);
	}

	@Override
	public int size() {
		return sequence.size();
	}

	public int indexOf(Base base) {
		for (int i = 0; i < sequence.size(); i++) {
			if (base.toString().equals(sequence.get(i).toString())) {
				return i;
			}
		}
		return -1;
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

