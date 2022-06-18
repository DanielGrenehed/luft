package api.luft_api.temperature;

import api.luft_api.sensor.SensorID;
import api.luft_api.websocket.SensorSubscriberWebSocketHandler;
import com.google.gson.Gson;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public class TemperatureWSHandler extends SensorSubscriberWebSocketHandler {

	private static TemperatureWSHandler singleton;

	public TemperatureWSHandler() {
		singleton = this;
	}
	public static void sendBySensorID(Temperature temp, SensorID sid) throws IOException {
		TextMessage message = new TextMessage(gson.toJson(temp));
		singleton.sendMessageBySensorID(message, sid);
	}

}
