package api.luft_api.controllers;

import api.luft_api.electricity.ElectricityDAO;
import api.luft_api.sensor.Sensor;
import api.luft_api.sensor.SensorDAO;
import api.luft_api.zone.Zone;
import api.luft_api.zone.ZoneDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class DashController {

	private ZoneDAO zdao = new ZoneDAO();
	private SensorDAO sdao = new SensorDAO();
	private ElectricityDAO edao = new ElectricityDAO();

	@RequestMapping("/")
	public String showDash(Model model) throws SQLException {

		model.addAttribute("zones", zdao.getZones());
		return "zones_dash";
	}

	@RequestMapping("/dash/{id}")
	public String showZoneDash(@PathVariable int id, Model model) throws SQLException {
		Zone zone = zdao.getZone(id);
		List<Sensor> sensors = sdao.getSensorsInZone(zone.getId());
		float electricity_price = edao.getLatestPrice().getPrice();

		model.addAttribute("ZoneName", zone.getName());
		model.addAttribute("ElectricityPrice", electricity_price);
		model.addAttribute("Sensors", sensors);
		return "dash";
	}
}
