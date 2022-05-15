package api.luft_api.humidity;

import java.util.Date;

public class HumidityInsertion {
	private String token;
	private Date time;
	private float humidity;

	public HumidityInsertion(String token, Date time, float humidity) {
		this.token = token;
		this.time = time;
		this.humidity = humidity;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String sensor_token) {
		this.token = sensor_token;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
}
