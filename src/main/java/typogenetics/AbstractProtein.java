package typogenetics;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProtein extends AbstractList<AminoAcid> {

	private final List<AminoAcid> aminoAcids;

	public AbstractProtein() {
		aminoAcids = new ArrayList<>();
	}

	@Override
	public boolean add(AminoAcid aminoAcid) {
		return aminoAcids.add(aminoAcid);
	}

	@Override
	public void clear() {
		aminoAcids.clear();
	}

	@Override
	public AminoAcid get(int index) {
		return aminoAcids.get(index);
	}

	@Override
	public int size() {
		return aminoAcids.size();
	}

}

