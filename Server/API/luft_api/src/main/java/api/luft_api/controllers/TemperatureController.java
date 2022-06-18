package api.luft_api.controllers;

import api.luft_api.temperature.Temperature;
import api.luft_api.temperature.TemperatureDAO;
import api.luft_api.temperature.TemperatureInsertion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	@GetMapping("/temperatures/today/{id}")
	public List<Temperature> getAveragesForToday(@PathVariable int id) throws SQLException {
		return dao.getAveragesForDate(new Date(), id);
	}

	@GetMapping("/temperatures/{date}/{id}")
	public List<Temperature> getAveragesForDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @PathVariable int id) throws SQLException {
		return dao.getAveragesForDate(date, id);
	}

	private static final Date zdate = new Date(1700, 1, 1);
	@GetMapping("/temperatures/averages/{id}")
	public List<Temperature> getAveragesForPeriod(@PathVariable int id, @RequestParam(name = "start", required = false) String s_start, @RequestParam(name = "end", required = false) String s_end) throws SQLException {
		Date start = s_start == null ? zdate:null , end = s_end == null ? new Date() : null;
		try {
			start = start == null ? new SimpleDateFormat("yyyy-MM-dd").parse(s_start) : start;
			end = end == null ? new SimpleDateFormat("yyyy-MM-dd").parse(s_end) : end;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dao.getDailyAveragesForPeriod(id, start, end);
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
