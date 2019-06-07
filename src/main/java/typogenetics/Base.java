package typogenetics;

public class Base {

	enum Letter {
		A, C, G, T;
	}

	enum Derivative {
		purine, pyrimidine;
	}

	private Letter letter;
	private Derivative derivative;

	public Base(Letter letter) {
		this.setLetter(letter);
	}

	public Base(char letter) {
		this.setLetter(parseLetter(letter));
	}

	public void setLetter(Letter letter) {
		this.letter = letter;

		if (letter == Letter.A || letter == Letter.G) {
			this.derivative = Derivative.purine;
		}
		else {
			this.derivative = Derivative.pyrimidine;
		}
	}

	public Letter getLetter() {
		return letter;
	}

	public Derivative getDerivative() {
		return derivative;
	}

	public Base getComplement() {
		switch (letter) {
			case A: return new Base('T');
			case T: return new Base('A');
			case C: return new Base('G');
			case G: return new Base('C');
			default: return null;
		}
	}

	@Override
	public String toString() {
		switch (getLetter()) {
			case A: return "A";
			case C: return "C";
			case G: return "G";
			case T: return "T";
			default: return "";
		}
	}

	public static Letter parseLetter(char letter) {
		switch (letter) {
			case 'A': return Letter.A;
			case 'C': return Letter.C;
			case 'G': return Letter.G;
			case 'T': return Letter.T;
			default : throw new IllegalArgumentException("Invalid base type");
		}
	}

}

