package typogenetics;

import typogenetics.AminoAcid.Direction;

public class Enzyme extends AbstractProtein {

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

}

