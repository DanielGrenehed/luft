package api.luft_api.controllers;

import api.luft_api.temperature.Temperature;
import api.luft_api.temperature.TemperatureDAO;
import api.luft_api.temperature.TemperatureInsertion;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class TemperatureController {
	private TemperatureDAO dao = new TemperatureDAO();

	@GetMapping("/temperature/{id}")
	public Temperature getLatestTemperature(@PathVariable int id) throws SQLException {
		return dao.getLatestTemperature(id);
	}

	@GetMapping("/temperatures/{id}")
	public List<Temperature> getAverageTemperatures(@PathVariable int id) throws SQLException {
		return dao.getAverageTemperatures(id);
	}

	@GetMapping("/zone/{id}/temperature")
	public Temperature getLatestZoneTemperature(@PathVariable int id) throws SQLException {
		return dao.getLatestZoneValue(id);
	}

	@GetMapping("/temperature/set/{temp}")
	public Temperature postTemperature(@PathVariable float temp) throws SQLException {
		return dao.postTemperature(temp);
	}

	@PostMapping("/temperature")
	public Temperature insertTemperature(@RequestBody TemperatureInsertion insert) throws SQLException {
		return dao.postTemperature(insert);
	}
}
