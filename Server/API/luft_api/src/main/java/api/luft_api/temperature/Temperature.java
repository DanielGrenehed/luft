package api.luft_api.temperature;

import api.luft_api.controllers.TemperatureController;

import java.util.Date;

public class Temperature {

	private Date time;
	private float temperature;

	public Temperature(float temperature, Date time) {
		this.temperature = temperature;
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
}
