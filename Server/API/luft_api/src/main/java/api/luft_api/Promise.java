package api.luft_api;

public class Promise {

	private Object result = null;

	public Promise() {};

	public boolean isSet() {
		return result != null;
	}

	public void set(Object o) {
		result = o;
	}

	public Object get() {
		return result;
	}

}
