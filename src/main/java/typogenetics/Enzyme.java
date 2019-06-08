package typogenetics;

import java.util.ArrayList;
import java.util.Arrays;
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

	public List<Strand> bind(Strand input) {
		List<Strand> products = new ArrayList<>();

		// While the enzyme works on the protein the following are tracked.
		// 1) Whether copy mode is on or off
		copy = false;
		// 2) The strand to which it is currently attached
		strand = input;
		// 3) The complementary strand (if there is one)
		complement = new Strand();
		// 3) The base to which it is currently bound
		binding = strand.indexOf(getBindingBase()); // TODO this should be random?
		// Each amino acid will affect at least one of the variables above

		final Command[] movements = { Command.mvr, Command.mvl, Command.rpy,
									  Command.rpu, Command.lpy, Command.lpu,
									  Command.swi };

		final Command[] amendments = { Command.ina, Command.inc, Command.ing,
									   Command.ist, Command.del };

		for (int i = 0; i < size(); i++) {

			if (!isAttached()) {
				if (!strand.isEmpty())
					products.add(strand);
				if (!complement.isEmpty())
					products.add(complement);

				return products;
			}

			Command command = get(i).getCommand();
			consolePrint(command);

			if (Arrays.asList(movements).contains(command)) {
				move(command);
			} else if (Arrays.asList(amendments).contains(command)) {
				amend(command);
			} else if (command == Command.cop) {
				copy = true;
			} else if (command == Command.off) {
				copy = false;
			} else if (command == Command.cut) {

			} else if (command == Command.swi) {
				complement = strand;
				strand = new Strand();
			}
		}

		products.add(strand);
		products.add(complement);
		return products;
	}

	private void move(Command command) {
		// mvr - move one unit to the right
		// mvl - move one unit to the left
		// rpy - search for the nearest pyrimidine to the right
		// rpu - search for the nearest purine to the right
		// lpy - search for the nearest pyrimidine to the left
		// lpu - search for the nearest purine to the left
		// swi - switch enzyme to other strand

		if (command == Command.mvr) {
			binding++;

			if (copy && isAttached()) {
				Base current = strand.get(binding);
				complement.add(current.getComplement());
			}
		}
		else if (command == Command.mvl) {
			binding--;

			if (copy && isAttached()) {
				Base current = strand.get(binding);
				complement.add(current.getComplement());
			}
		}
		else if (command == Command.rpy) {
			while(true) {
				binding++;

				if (isAttached()) {
					Base current = strand.get(binding);
					if (copy) {
						complement.add(current.getComplement());
					}
					if (current.getDerivative() == Derivative.pyrimidine) {
						return;
					}
				} else {
					return;
				}
			}
		}
		else if (command == Command.rpu) {
			while(true) {
				binding++;

				if (isAttached()) {
					Base current = strand.get(binding);
					if (copy) {
						complement.add(current.getComplement());
					}
					if (current.getDerivative() == Derivative.purine) {
						return;
					}
				} else {
					return;
				}
			}
		}
		else if (command == Command.lpy) {
			while(true) {
				binding--;

				if (isAttached()) {
					Base current = strand.get(binding);
					if (copy) {
						complement.add(0, current.getComplement());
					}
					if (current.getDerivative() == Derivative.pyrimidine) {
						return;
					}
				} else {
					return;
				}
			}
		}
		else if (command == Command.lpu) {
			while(true) {
				binding--;

				if (isAttached()) {
					Base current = strand.get(binding);
					if (copy) {
						complement.add(0, current.getComplement());
					}
					if (current.getDerivative() == Derivative.purine) {
						return;
					}
				} else {
					return;
				}
			}
		}
	}

	private Protein amend(Command command) {
		// ina - insert A to the right of this unit
		// inc - insert C to the right of this unit
		// ing - insert G to the right of this unit
		// ist - insert T to the right of this unit
		// del - delete base from strand

		if (command == Command.del) {
		}
		else if (command == Command.ina) {
		}
		else if (command == Command.inc) {
		}
		else if (command == Command.ing) {
		}
		else if (command == Command.ist) {
		}
		return new Protein();
	}

	public String getCommands() {
		String commands = "";
		for (int i = 0; i < size(); i++) {
			commands += get(i).getCommand() + " ";
		}
		return commands;
	}

	private void consolePrint(Command command) {
		System.out.print("Copy " + (copy ? "on" : "off"));
		System.out.println(", executing " + command);
		System.out.print(strand);
		System.out.println("  " + complement);

		for (int i = 0; i < binding; i++) {
			System.out.print("   ");
		}
		System.out.println(" |\n");
	}

	private boolean isAttached() {
		// If false, enzyme has "fallen off" or failed to attach
		return (binding >= 0 && binding < strand.size());
	}

}

