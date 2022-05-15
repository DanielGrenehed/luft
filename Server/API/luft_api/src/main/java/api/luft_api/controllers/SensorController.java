package api.luft_api.controllers;

import api.luft_api.humidity.Humidity;
import api.luft_api.humidity.HumidityInsertion;
import api.luft_api.sensor.ReadingInsertion;
import api.luft_api.sensor.Sensor;
import api.luft_api.sensor.SensorDAO;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class SensorController {

	private SensorDAO dao = new SensorDAO();

	@GetMapping("/zone/{id}")
	public List<Sensor> getSensorsInZone(@PathVariable int id) throws SQLException {
		return dao.getSensorsInZone(id);
	}

	@PostMapping("/sensor")
	public boolean insertHumidity(@RequestBody ReadingInsertion insert) throws SQLException {
		return dao.postReading(insert);
	}
}
