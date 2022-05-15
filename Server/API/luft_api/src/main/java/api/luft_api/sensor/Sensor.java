package api.luft_api.sensor;

public class Sensor {
	private int id;
	private String token, sensor_name;

	public Sensor(int id, String token, String name) {
		this.id = id;
		this.token = token;
		this.sensor_name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSensor_name() {
		return sensor_name;
	}

	public void setSensor_name(String sensor_name) {
		this.sensor_name = sensor_name;
	}
}
