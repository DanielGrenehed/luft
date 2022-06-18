package api.luft_api.websocket;

import api.luft_api.humidity.HumidityWSHandler;
import api.luft_api.temperature.TemperatureWSHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new HumidityWSHandler(), "/humidity-ws");
		registry.addHandler(new TemperatureWSHandler(), "/temperature-ws");
	}
}
