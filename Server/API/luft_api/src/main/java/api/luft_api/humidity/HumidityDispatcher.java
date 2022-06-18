package api.luft_api.humidity;

import api.luft_api.sensor.Sensor;
import api.luft_api.sensor.SensorID;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class HumidityDispatcher {

	private class HIDUnion {
		private Humidity humidity;
		private SensorID sensor_id;
		public HIDUnion(Humidity h, SensorID s) {
			humidity =h;
			sensor_id = s;
		}

		public Humidity getHumidity() {
			return humidity;
		}

		public SensorID getSensorID() {
			return sensor_id;
		}
	}

	private static HumidityDispatcher singleton = new HumidityDispatcher();

	public static HumidityDispatcher getInstance() {
		return singleton;
	}

	private Queue<HIDUnion> dispatch_queue = new LinkedList<>();

	public synchronized void dispatchHumidity(Humidity h, SensorID s) {
		dispatch_queue.add(new HIDUnion(h, s));
	}

	public synchronized void processDispatch() {
		while(!dispatch_queue.isEmpty()) {
			HIDUnion hu = dispatch_queue.poll();
			try {
				HumidityWSHandler.sendBySensorID(hu.getHumidity(), hu.getSensorID());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
