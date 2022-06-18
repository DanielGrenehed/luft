package api.luft_api.humidity;

import java.util.Date;

public class AverageHumidity extends Humidity {

	private float min, max;



	public AverageHumidity(float humidity, Date time, float min, float max) {
		super(humidity, time);
		this.min = min;
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}
}
