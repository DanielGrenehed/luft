package api.luft_api.electricity;

import java.util.Date;

public class Electricity {
	private Date time;
	private float price;

	public Electricity(float price, Date time) {
		this.price = price;
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
