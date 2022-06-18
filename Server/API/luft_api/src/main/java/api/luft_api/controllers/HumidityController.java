package api.luft_api.controllers;

import api.luft_api.humidity.AverageHumidity;
import api.luft_api.humidity.Humidity;
import api.luft_api.humidity.HumidityDAO;
import api.luft_api.humidity.HumidityInsertion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	@GetMapping("/humidities/today/{id}")
	public List<Humidity> getAveagesForToday(@PathVariable int id) throws SQLException {
		return dao.getAveragesForDate(new Date(), id);
	}

	@GetMapping("/humidities/{date}/{id}")
	public List<Humidity> getAveragesForDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @PathVariable int id) throws SQLException {
		return dao.getAveragesForDate(date, id);
	}
	private static final Date zdate = new Date(0, 0, 1);
	@GetMapping("/humidities/averages/{id}")
	public List<AverageHumidity> getAveragesForPeriod(@PathVariable int id, @RequestParam(name = "start", required = false) String s_start, @RequestParam(name = "end", required = false) String s_end) throws SQLException {
		Date start = s_start == null ? zdate: null, end = s_end == null ? new Date() : null;
		try {
			start = start == null ? new SimpleDateFormat("yyyy-MM-dd").parse(s_start) : start;
			end = end == null ? new SimpleDateFormat("yyyy-MM-dd").parse(s_end) : end;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return dao.getDailyAveragesForPeriod(id, start, end);
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
