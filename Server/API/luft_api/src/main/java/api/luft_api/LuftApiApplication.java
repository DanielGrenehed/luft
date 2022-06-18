package api.luft_api;

import api.luft_api.humidity.HumidityDispatcher;
import api.luft_api.temperature.TemperatureDispatcher;
import api.luft_api.websocket.DispatchHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuftApiApplication {


	public static void main(String[] args) {
		DispatchHandler dh = new DispatchHandler();
		Thread t = new Thread(dh);
		t.start();
		SpringApplication.run(LuftApiApplication.class, args);
	}

}
