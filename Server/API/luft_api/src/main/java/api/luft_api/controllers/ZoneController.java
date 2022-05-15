package api.luft_api.controllers;

import api.luft_api.zone.Zone;
import api.luft_api.zone.ZoneDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ZoneController {
	private ZoneDAO dao = new ZoneDAO();

	@GetMapping("/zones")
	public List<Zone> getZones() throws SQLException {
		return dao.getZones();
	}
}
