package typogenetics;

public class Enzyme extends AbstractProtein {

	private Base bindingPreference;

	public Enzyme() {
		setBindingPreference();
	}

	public void setBindingPreference() {
	}

	public Base getBindingPreference() {
		return bindingPreference;
	}

}

