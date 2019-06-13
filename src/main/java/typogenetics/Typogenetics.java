package typogenetics;

import java.util.List;
import java.util.Scanner;

public class Typogenetics {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter base sequence: (ctrl-C to exit)");
		while (scanner.hasNext()) {
			String input = scanner.next();
			Strand strand;
			try {
				strand = new Strand(input);
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid base.");
				continue;
			}

			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			enzyme.setOption("output", "true");

			if (enzyme.isEmpty()) {
				System.err.println("No enzymes.");
				input = scanner.next();
				continue;
			}

			System.out.println("Enzyme: " + enzyme.getCommands());
			System.out.println("Ternary structure: " + enzyme.getBindingBase());

			List<Strand> strands = enzyme.bind(strand);

			System.out.println("Resulting strands: ");
			for (Strand str : strands)
				System.out.println(str);

			input = scanner.next();
		}

		scanner.close();
	}

}

