package typogenetics;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Typogenetics {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter base sequence: (ctrl-C to exit)");
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();

			Strand strand;
			try {
				strand = new Strand(input);
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid base.");
				continue;
			}

			// There should probably be an option to select an enzyme other
			// than the first one produced by the ribosome...
			Enzyme enzyme = Ribosome.translate(strand, Enzyme.class).get(0);
			enzyme.setOption("output", "true");

			if (enzyme.isEmpty()) {
				System.err.println("No enzymes.");
				continue;
			}

			Base preference = enzyme.getBindingBase();
			System.out.println("Enzyme: " + enzyme.getCommands());
			System.out.println("This enzyme can bind on base " + preference);

			System.out.println("On which " + preference + " base should it " 
					+ "bind? (0, 1, 2, 3, ...) ");

			int offset = 0;
			boolean valid = false;

			while (!valid) {
				try {
					offset = scanner.nextInt();
					valid = true;
				} catch (InputMismatchException e) {
					System.err.println("Enter a positive integer.");
					scanner.next();
				}
			}

			List<Strand> strands = enzyme.bind(strand, offset);

			System.out.println("Resulting strands: ");
			for (Strand str : strands)
				System.out.println(Strand.formatStrand(str.toString()));

			System.out.println("\nFinished.\nNew sequence: (ctrl-C to exit)");
			scanner.nextLine();
		}

		scanner.close();
	}

}

