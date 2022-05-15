package api.luft_api.controllers;

import api.luft_api.electricity.Electricity;
import api.luft_api.electricity.ElectricityDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class ElectricityController {

	private ElectricityDAO dao = new ElectricityDAO();

	@GetMapping("/electricity")
	public Electricity getLatestPrice() throws SQLException {
		return dao.getLatestPrice();
	}

	@PostMapping("/electricity")
	public Electricity postNewPrice(@RequestBody Electricity price) throws SQLException {
		return dao.postElectricity(price);
	}
}
