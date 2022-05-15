package api.luft_api.sensor;

import java.util.Date;

public class ReadingInsertion {
	private String token;
	private float temperature;
	private float humidity;
	private Date time;

	public ReadingInsertion(String token, float temp, float humid, Date time) {
		this.token = token;
		temperature = temp;
		humidity = humid;
		this.time = time;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
