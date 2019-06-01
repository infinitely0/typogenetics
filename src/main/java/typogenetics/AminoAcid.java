package typogenetics;

public class AminoAcid {

	enum Command { spa, cut, dlt, swi,
				   mvr, mvl, cop, off,
				   ina, inc, ing, itt,
				   rpy, rpu, lpy, lpu };

	enum Direction { l, r, s };

	private Duplet duplet;
	private Command command;
	private Direction direction;

	public AminoAcid(Base first, Base second) {
		duplet = new Duplet(first, second);
		setCommand();
		setDirection();
	}

	private void setCommand() {
		switch (duplet.toString()) {
			case "AA": command = Command.spa; break;
			case "AC": command = Command.cut; break;
			case "AG": command = Command.dlt; break;
			case "AT": command = Command.swi; break;
			case "CA": command = Command.mvr; break;
			case "CC": command = Command.mvl; break;
			case "CG": command = Command.cop; break;
			case "CT": command = Command.off; break;
			case "GA": command = Command.ina; break;
			case "GC": command = Command.inc; break;
			case "GG": command = Command.ing; break;
			case "GT": command = Command.itt; break;
			case "TA": command = Command.rpy; break;
			case "TC": command = Command.rpu; break;
			case "TG": command = Command.lpy; break;
			case "TT": command = Command.lpu; break;
		}
	}

	public Command getCommand() {
		return command;
	}

	private void setDirection() {
		switch (duplet.toString()) {
			case "AA": direction = null; break;
			case "AC": direction = Direction.s; break;
			case "AG": direction = Direction.s; break;
			case "AT": direction = Direction.r; break;
			case "CA": direction = Direction.s; break;
			case "CC": direction = Direction.s; break;
			case "CG": direction = Direction.r; break;
			case "CT": direction = Direction.l; break;
			case "GA": direction = Direction.s; break;
			case "GC": direction = Direction.r; break;
			case "GG": direction = Direction.r; break;
			case "GT": direction = Direction.l; break;
			case "TA": direction = Direction.r; break;
			case "TC": direction = Direction.l; break;
			case "TG": direction = Direction.l; break;
			case "TT": direction = Direction.l; break;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return duplet.toString();
	}

}

class Duplet {

	private Base first;
	private Base second;

	public Duplet(Base first, Base second) {
		this.first = first;
		this.second = second;
	}

	public Base getFirst() {
		return first;
	}

	public Base getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return first.toString() + second.toString();
	}

}

