package api.luft_api.humidity;

import api.luft_api.sensor.SensorID;
import api.luft_api.websocket.SensorSubscriberWebSocketHandler;
import com.google.gson.Gson;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public class HumidityWSHandler extends SensorSubscriberWebSocketHandler {

	private static HumidityWSHandler singleton;

	public HumidityWSHandler() {
		singleton = this;
	}
	public static void sendBySensorID(Humidity humidity, SensorID sid) throws IOException {
		TextMessage message = new TextMessage(gson.toJson(humidity));
		singleton.sendMessageBySensorID(message, sid);
	}

}
