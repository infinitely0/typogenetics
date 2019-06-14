package typogenetics;

import java.util.ArrayList;
import java.util.List;

public final class Ribosome {

	public static <T> List<T> translate(Strand strand, Class<T> type) {
		List<T> list = new ArrayList<>();
		AbstractProtein protein;

		if (type == Protein.class) {
			 protein = new Protein();
		} else if (type == Enzyme.class) {
			protein = new Enzyme();
		} else {
			throw new IllegalArgumentException("Invalid protein type");
		}

		if (strand.size() % 2 == 1) {
			strand.remove(strand.size() - 1);
		}

		for (int i = 0; i < strand.size(); i++) {
			Base first = strand.get(i);
			Base second = strand.get(++i);

			AminoAcid aa = new AminoAcid(first, second);

			if (aa.getCommand() == AminoAcid.Command.spa) {
				list.add(type.cast(protein));
				protein.clear();
			} else {
				protein.add(aa);
			}
		}
		list.add(type.cast(protein));

		return list;
	}

}

