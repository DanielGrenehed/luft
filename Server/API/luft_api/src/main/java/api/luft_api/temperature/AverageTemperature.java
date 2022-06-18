package api.luft_api.temperature;

import java.util.Date;

public class AverageTemperature extends Temperature {

	private float min, max;

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


	public AverageTemperature(float temperature, Date time, float min, float max) {
		super(temperature, time);
		this.min = min;
		this.max = max;
	}
}
