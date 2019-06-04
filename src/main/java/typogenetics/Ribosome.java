package typogenetics;

public final class Ribosome {

	@SuppressWarnings("unchecked")
	public static <T extends AbstractProtein> T translate(Strand strand, Class<?> type) {

		AbstractProtein protein;

		if (type == Protein.class) {
			protein = new Protein();
		} else if (type == Enzyme.class) {
			protein = new Enzyme();
		} else {
			throw new IllegalArgumentException("Invalid protein type");
		}

		for (int i = 0; i < strand.size(); i++) {
			Base first = strand.get(i);
			Base second = strand.get(++i);

			AminoAcid aa = new AminoAcid(first, second);
			protein.add(aa);
		}

		return (T) protein;
	}

}

