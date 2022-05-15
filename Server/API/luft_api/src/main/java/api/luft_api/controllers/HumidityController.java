package api.luft_api.controllers;

import api.luft_api.humidity.Humidity;
import api.luft_api.humidity.HumidityDAO;
import api.luft_api.humidity.HumidityInsertion;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class HumidityController {

	private HumidityDAO dao = new HumidityDAO();

	@GetMapping("/humidity/{id}")
	public Humidity getLatestHumidity(@PathVariable int id) throws SQLException {
		return dao.getLatestValue(id);
	}

	@GetMapping("/humidities/{id}")
	public List<Humidity> getAverageHumidity(@PathVariable int id) throws SQLException {
		return dao.getAverageValues(id);
	}

	@GetMapping("/zone/{id}/humidity")
	public Humidity getLatestZoneHumidity(@PathVariable int id) throws SQLException {
		return dao.getLatestZoneValue(id);
	}

	@GetMapping("/humidity/set/{humid}")
	public Humidity postHumidity(@PathVariable float humid) throws SQLException {
		return dao.postHumidity(humid);
	}

	@PostMapping("/humidity")
	public Humidity insertHumidity(@RequestBody HumidityInsertion insert) throws SQLException {
		return dao.postHumidity(insert);
	}

}
