package api.luft_api.temperature;

import api.luft_api.sensor.SensorID;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class TemperatureDispatcher {

	private class TIDUnion {
		private Temperature temperature;
		private SensorID sensor_id;
		public TIDUnion(Temperature t, SensorID sid) {
			temperature = t;
			sensor_id = sid;
		}
		public Temperature getTemperature() {
			return temperature;
		}
		public SensorID getSensorID() {
			return sensor_id;
		}
	}

	private static TemperatureDispatcher singleton = new TemperatureDispatcher();
	public static TemperatureDispatcher getInstance() {
		return singleton;
	}
	private Queue<TIDUnion> dispatch_queue = new LinkedList<>();

	public synchronized void dispatchTemperature(Temperature t, SensorID s) {
		dispatch_queue.add(new TIDUnion(t, s));
	}

	public synchronized void processDispatch() {
		while (!dispatch_queue.isEmpty()) {
			TIDUnion tu = dispatch_queue.poll();
			try {
				TemperatureWSHandler.sendBySensorID(tu.getTemperature(), tu.getSensorID());
				//System.out.println(tu);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
