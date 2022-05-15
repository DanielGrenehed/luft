package api.luft_api.zone;

import api.luft_api.Promise;
import api.luft_api.database.DataBaseAccessObject;
import api.luft_api.database.SharedDBAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {

	private DataBaseAccessObject dbao = SharedDBAO.getInstance();

	private Zone getFromSet(ResultSet set) throws SQLException {
		return new Zone(set.getString("tag"), set.getInt("id"), set.getInt("sensor_count"));
	}

	public List<Zone> getZones() throws SQLException {
		List<Zone> result = new ArrayList<>();
		dbao.Query("SELECT sensor_zone.id, sensor_zone.tag, COUNT(sensor.zone_id) AS sensor_count FROM luft_sc.sensor_zone LEFT JOIN luft_sc.sensor ON sensor_zone.id = sensor.zone_id GROUP BY sensor_zone.id, sensor_zone.tag ORDER BY sensor_count DESC;", set -> {
			while (set.next()) result.add(getFromSet(set));
		});
		return result;
	}

	public Zone getZone(int zone_id) throws SQLException {
		Promise result = new Promise();
		PreparedStatement stmt = dbao.prepareStatement("SELECT sensor_zone.id, sensor_zone.tag, COUNT(sensor.zone_id) AS sensor_count FROM luft_sc.sensor_zone LEFT JOIN luft_sc.sensor ON sensor_zone.id = sensor.zone_id WHERE sensor_zone.id=? GROUP BY sensor_zone.id, sensor_zone.tag ORDER BY sensor_zone.id, sensor_zone.tag DESC;");
		stmt.setInt(1, zone_id);
		dbao.Query(stmt, set -> {
			while (set.next()) result.set(getFromSet(set));
		});
		return (Zone) result.get();
	}

}
