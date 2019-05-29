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

	public Base(char letter) throws InvalidBaseException {
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

	public String toString() {
		switch (getLetter()) {
			case A: return "A";
			case C: return "C";
			case G: return "G";
			case T: return "T";
			default: return "";
		}
	}

	public static Letter parseLetter(char letter) throws InvalidBaseException {
		switch (letter) {
			case 'A': return Letter.A;
			case 'C': return Letter.C;
			case 'G': return Letter.G;
			case 'T': return Letter.T;
			default : throw new InvalidBaseException();
		}
	}

}

class InvalidBaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBaseException() {
		super("Invalid base");
	}

}

