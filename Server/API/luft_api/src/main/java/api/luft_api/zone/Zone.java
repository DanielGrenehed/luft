package api.luft_api.zone;

public class Zone {

	private String name;
	private int id;
	private int sensor_count;
	public Zone(String tag, int id, int sensor_count) {
		name = tag;
		this.id = id;
		this.sensor_count = sensor_count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSensor_count() {
		return sensor_count;
	}

	public void setSensor_count(int sensor_count) {
		this.sensor_count = sensor_count;
	}




}
