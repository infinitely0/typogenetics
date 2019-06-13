package typogenetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import typogenetics.AminoAcid.Command;
import typogenetics.AminoAcid.Direction;
import typogenetics.Base.Derivative;

public class Enzyme extends AbstractProtein {

	// Copy mode on/off
	private boolean copy;
	// The strand to which this enzyme is currently bound
	private Strand strand;
	// Where on the strand it is bound
	private int binding;
	// The complementary strand
	private Strand complement;
	// The strands yielded by the enzyme
	private List<Strand> products = new ArrayList<>();

	private HashMap<String, String> options = new HashMap<>();

	// Binding preference of this enzyme based on tertiary structure
	public Base getBindingBase() {
		int orientation;
		AminoAcid first = get(0);

		if (first.getDirection() == Direction.l) {
			orientation = 270;
		} else if (first.getDirection() == Direction.r) {
			orientation = 90;
		} else {
			orientation = 0;
		}

		int rotation = 0;

		if (size() > 1) {
			for (int i = 1; i < size(); i++) {
				AminoAcid aa = get(i);
				Direction direction = aa.getDirection();

				if (direction == Direction.l) {
					rotation = Math.floorMod(rotation - 90, 360);
				} else if (direction == Direction.r) {
					rotation = (rotation + 90) % 360;
				}
			}
		}

		int relativeOrientation = Math.floorMod(rotation - orientation, 360);
		Base bindingBase;

		if (relativeOrientation == 0) {
			bindingBase = new Base('A');
		} else if (relativeOrientation == 90) {
			bindingBase = new Base('G');
		} else if (relativeOrientation == 270) {
			bindingBase = new Base('C');
		} else if (relativeOrientation == 180) {
			bindingBase = new Base('T');
		} else {
			return null;
		}

		return bindingBase;
	}

	// Bind to a strand and execute amino acids commands
	public List<Strand> bind(Strand input) {
		// While the enzyme works on the protein the following are tracked.
		// 1) Whether copy mode is on or off
		copy = false;
		// 2) The strand to which it is currently attached
		strand = input;
		// 3) The complementary strand (if there is one)
		complement = strand.emptyClone();
		// 3) The base to which it is currently bound
		binding = strand.indexOf(getBindingBase()); // TODO this should be random?
		// Each amino acid will affect at least one of the variables above

		final Command[] movements = { Command.mvr, Command.mvl };

		final Command[] searches = { Command.rpy, Command.rpu,
									 Command.lpy, Command.lpu };

		final Command[] amendments = { Command.ina, Command.inc, Command.ing,
									   Command.ist };

		for (int i = 0; i < size(); i++) {

			if (!isAttached()) {
				isolateStrands();
				return products;
			}

			Command command = get(i).getCommand();

			if (Boolean.parseBoolean(options.get("output"))) {
				consolePrint();
				System.out.print("Copy " + (copy ? "on" : "off"));
				System.out.println(", executing " + command);
			}

			// movement commands
			if (Arrays.asList(movements).contains(command)) {
				move(command);
			// search commands
			} else if (Arrays.asList(searches).contains(command)) {
				search(command);
			// insert commands
			} else if (Arrays.asList(amendments).contains(command)) {
				insert(command);
			// del - delete base from strand
			} else if (command == Command.del) {
				delete();
			// cop - turn on copy mode
			} else if (command == Command.cop) {
				copy = true;
			// off - turn off copy mode
			} else if (command == Command.off) {
				copy = false;
			// cut - cut strands
			} else if (command == Command.cut) {
				cut();
			// swi - switch enzyme to other strand
			} else if (command == Command.swi) {
				swi();
			}
		}

		if (Boolean.parseBoolean(options.get("output")))
			consolePrint();

		isolateStrands();
		return products;
	}

	public String getCommands() {
		String commands = "";
		for (int i = 0; i < size(); i++) {
			commands += get(i).getCommand() + " ";
		}
		return commands;
	}

	public void setOption(String key, String value) {
		options.put(key, value);
	}

	// Move commands:
	// mvr - move one unit to the right
	// mvl - move one unit to the left
	private void move(Command command) {
		if (command == Command.mvr)
			binding++;
		else if (command == Command.mvl)
			binding--;

		if (copy && isAttached() && isOnBase()) {
			Base current = strand.get(binding);
			complement.set(binding, current.getComplement());
		}
	}

	// Search commands:
	// rpy - search for the nearest pyrimidine to the right
	// rpu - search for the nearest purine to the right
	// lpy - search for the nearest pyrimidine to the left
	// lpu - search for the nearest purine to the left
	private void search(Command command) {
		switch (command) {
			case rpy: searchFor(Direction.r, Derivative.pyrimidine); break;
			case rpu: searchFor(Direction.r, Derivative.purine); break;
			case lpy: searchFor(Direction.l, Derivative.pyrimidine); break;
			case lpu: searchFor(Direction.l, Derivative.purine); break;
			default: ;
		}
	}

	private void searchFor(Direction direction, Derivative derivative) {
		while(true) {
			if (direction == Direction.r)
				binding++;
			else if (direction == Direction.l)
				binding--;

			if (!isAttached())
				break;

			Base current = strand.get(binding);
			if (copy && isOnBase()) {
				complement.set(binding, current.getComplement());
			}

			if (current.getDerivative() == derivative)
				break;
		}
	}

	// Insert commands:
	// ina - insert A to the right of this unit
	// inc - insert C to the right of this unit
	// ing - insert G to the right of this unit
	// ist - insert T to the right of this unit
	private void insert(Command command) {
		Base base;

		switch (command) {
			case ina: base = new Base('A'); break;
			case inc: base = new Base('C'); break;
			case ing: base = new Base('G'); break;
			case ist: base = new Base('T'); break;
			default: throw new IllegalArgumentException();
		}

		binding++;
		insertBase(strand, base);
		if (copy)
			insertBase(complement, base.getComplement());
		else
			complement.add(null); // Padding to keep strands same length
	}

	private void insertBase(Strand strand, Base base) {
		if (binding == strand.size())
			strand.add(base);
		else
			strand.add(binding, base);
	}

	// del - delete base from strand
	private void delete() {
		strand.remove(binding);
		if (copy)
			complement.remove(binding);
		else
			strand.add(null); // Padding to keep strands same length 
	}

	// cut - cut strands
	private void cut() {
		strand = replaceSegments(strand);
		if (copy) {
			complement = replaceSegments(complement);
		}

	}

	// swi - switch enzyme to other strand
	private void swi() {
		Strand strandCopy = strand.clone();
		Strand complementCopy = complement.clone();

		strand = complementCopy;
		complement = strandCopy;

		strand.reverse();
		complement.reverse();

		binding = strand.size() - binding - 1;
	}

	private void consolePrint() {
		String complementString = "-";
		for (int i = 0; i < complement.size(); i++) {
			if (complement.get(i) == null)
				complementString += " -";
			else
				complementString += complement.get(i) + "-";
		}
		System.out.println("\n" + complementString);

		String strandString = "-";
		for (int i = 0; i < strand.size(); i++) {
			if (strand.get(i) == null)
				strandString += " -";
			else
				strandString += strand.get(i) + "-";
		}
		System.out.println(strandString);

		String bindingString = " ";
		for (int i = 0; i < binding; i++) {
			bindingString += "  ";
		}
		System.out.println(bindingString + "|\n");
	}

	// If false, enzyme has "fallen off" or failed to attach
	private boolean isAttached() {
		return (binding >= 0 && binding < strand.size());
	}

	// If false, enzyme is not attached to a base (usually after a switch)
	private boolean isOnBase() {
		Base base = strand.get(binding);
		if (base == null)
			return false;
		else
			return true;
	}

	private void isolateStrands() {
		if (!strand.isEmpty()) {
			findSegments(strand);
		}

		if (!complement.isEmpty()) {
			complement.reverse();
			findSegments(complement);
		}
	}

	private void findSegments(Strand source) {
		Strand segment = new Strand();

		for (Base base : source) {
			if (base == null) {
				if (!segment.isEmpty()) {
					products.add(segment);
				}
				segment = new Strand();
			}
			else {
				segment.add(base);
			}
		}

		if (!segment.isEmpty())
			products.add(segment);
	}

	// When a strand is cut, strands to the right of the enzyme are released
	// This is done by replacing them with nulls after isolating them
	private Strand replaceSegments(Strand source) {
		Strand segment = new Strand();

		int from = binding + 1;
		for (int i = from; i < source.size(); i++) {
			Base base = source.get(i);

			if (base == null) {
				if (!segment.isEmpty()) {
					products.add(segment);
				}
				segment = new Strand();
			}
			else {
				segment.add(base);
				source.set(i, null);
			}
		}

		if (!segment.isEmpty())
			products.add(segment);

		return source;
	}

}

