package api.luft_api.websocket;

import api.luft_api.humidity.HumidityDispatcher;
import api.luft_api.temperature.TemperatureDispatcher;

public class DispatchHandler implements Runnable{
	private boolean running = false;

	public void stop() {
		running = false;
	}
	@Override
	public void run() {
		running = true;
		while (running) {
			TemperatureDispatcher.getInstance().processDispatch();
			HumidityDispatcher.getInstance().processDispatch();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
