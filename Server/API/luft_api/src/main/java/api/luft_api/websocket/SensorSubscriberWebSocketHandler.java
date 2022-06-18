package api.luft_api.websocket;

import api.luft_api.sensor.SensorID;
import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorSubscriberWebSocketHandler extends TextWebSocketHandler {

	private Map<WebSocketSession, SensorID> subscriber_map = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		try {
			subscriber_map.remove(session);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		System.out.println(status);
	}

	protected static Gson gson = new Gson();

	/*
	 * 	Subscribe session to sensor
	 * */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		SensorID sid = gson.fromJson(message.getPayload(), SensorID.class);
		System.out.println(sid.getSensor_id());
		System.out.println(message.getPayload());
		subscriber_map.put(session, sid);
	}

	protected void sendMessageBySensorID(TextMessage message, SensorID sid) throws IOException {
		//System.out.println(message);
		for(Map.Entry<WebSocketSession, SensorID> entry : subscriber_map.entrySet()) {
			if(entry.getValue().getSensor_id() == sid.getSensor_id()){
				entry.getKey().sendMessage(message);
				//System.out.println("sent");
			}
		}
	}

}
