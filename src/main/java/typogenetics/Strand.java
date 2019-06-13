package typogenetics;

import java.util.Collections;
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
	public void add(int index, Base base) {
		sequence.add(index, base);
	}

	@Override
	public Base set(int index, Base base) {
		return sequence.set(index, base);
	}

	@Override
	public boolean add(Base base) {
		return sequence.add(base);
	}

	@Override
	public int size() {
		return sequence.size();
	}

	@Override
	public Base remove(int index) {
		return sequence.remove(index);
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < sequence.size(); i++) {
			if (sequence.get(i) != null)
				return false;
		}
		return true;
	}

	public void reverse() {
		Collections.reverse(sequence);
	}

	public int indexOf(Base base) {
		for (int i = 0; i < sequence.size(); i++) {
			Base next = sequence.get(i);
			if (next == null)
				continue;
			else if (base.toString().equals(next.toString()))
				return i;
		}
		return -1;
	}

	public String toString() {
		List<Base> copy = new ArrayList<>(sequence);
		copy.removeAll(Collections.singleton(null));
		return copy.toString();
	}

	public Strand clone() {
		Strand copy = new Strand();
		for (Base base : sequence) {
			copy.add(base);
		}
		return copy;
	}

	public Strand emptyClone() {
		Strand complement = new Strand();
		for (int i = 0; i < size(); i++) {
			complement.add(null);
		}
		return complement;
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

