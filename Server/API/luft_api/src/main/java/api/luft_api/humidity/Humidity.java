package api.luft_api.humidity;

import java.util.Date;

public class Humidity {
	private Date time;
	private float humidity;

	public Humidity(float humidity, Date time) {
		this.humidity = humidity;
		this.time = time;
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
