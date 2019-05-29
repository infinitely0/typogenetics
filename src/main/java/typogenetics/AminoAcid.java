package typogenetics;

import java.util.Map;
import java.util.HashMap;

public class AminoAcid {

	final Map<String, String> AMINO_ACIDS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("AA", "spa");
			put("AC", "cut");
			put("AG", "dlt");
			put("AT", "swi");
			put("CA", "mvr");
			put("CC", "mvl");
			put("CG", "cop");
			put("CT", "off");
			put("GA", "ina");
			put("GC", "inc");
			put("GG", "ing");
			put("GT", "itt");
			put("TA", "rpy");
			put("TC", "rpu");
			put("TG", "lpy");
			put("TT", "lpu");
		}
	};

	private Pair<Base, Base> duplet;
	private String command;

	public AminoAcid(Base first, Base second) {
		this.duplet = new Pair<Base, Base>(first, second);
		this.setCommand();

	}

	public Pair<Base, Base> getDuplet() {
		return duplet;
	}

	private void setCommand() {
		this.command = AMINO_ACIDS.get(this.toString());
	}

	public String getCommand() {
		return this.command;
	}

	public String toString() {
		return duplet.getFirst().toString() + duplet.getSecond().toString();
	}

}

class Pair<F,S> {

	private final F first;
	private final S second;

	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

}

