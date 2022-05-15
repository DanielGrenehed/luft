package api.luft_api.temperature;

import javax.xml.crypto.Data;
import java.util.Date;

public class TemperatureInsertion {
	private String token;
	private float temperature;
	private Date time;

	public TemperatureInsertion(String token, float temperature, Date time) {
		this.token = token;
		this.temperature = temperature;
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
