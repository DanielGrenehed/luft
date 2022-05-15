package api.luft_api.temperature;

import api.luft_api.Promise;
import api.luft_api.database.DataBaseAccessObject;
import api.luft_api.database.SharedDBAO;
import api.luft_api.humidity.Humidity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemperatureDAO {

	private DataBaseAccessObject dbao = SharedDBAO.getInstance();

	private Temperature getFromSet(ResultSet set) throws SQLException {
		return new Temperature(set.getFloat("temperature"), set.getTimestamp("log_time"));
	}

	public Temperature getLatestTemperature(int sensor_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT * FROM luft_sc.temperature WHERE sensor_id=? ORDER BY log_time DESC LIMIT 1;");
		stmt.setInt(1, sensor_id);
		Promise result = new Promise();
		dbao.Query(stmt, set -> { while (set.next()) result.set(getFromSet(set)); });
		return (Temperature) result.get();
	}

	public List<Temperature> getAverageTemperatures(int sensor_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT sensor_id, CAST(log_time AS DATE), AVG(temperature) AS temperature FROM luft_sc.temperature WHERE sensor_id=? GROUP BY sensor_id, CAST(log_time AS DATE) ORDER BY log_time DESC;");
		stmt.setInt(1, sensor_id);
		List<Temperature> result = new ArrayList<>();
		dbao.Query(stmt, set -> { while (set.next()) result.add(getFromSet(set)); });
		return result;
	}

	public Temperature getLatestZoneValue(int zone_id) throws SQLException {
		Promise result = new Promise();
		PreparedStatement stmt = dbao.prepareStatement("SELECT * FROM luft_sc.temperature WHERE sensor_id IN (SELECT id FROM luft_sc.sensor WHERE zone_id=?) ORDER BY log_time DESC LIMIT 1;");
		stmt.setInt(1, zone_id);
		dbao.Query(stmt, set -> { while (set.next()) result.set(getFromSet(set)); });
		return (Temperature) result.get();
	}

	public Temperature postTemperature(float temp) throws SQLException {
		PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.temperature (temperature, sensor_id) values (?, 1);");
		stmt.setFloat(1,temp);
		Promise effect = new Promise();
		dbao.QueryUpdate(stmt, (affected, set) -> { if (affected > 0) while (set.next()) effect.set(getFromSet(set)); });
		return effect.isSet() ? (Temperature) effect.get() : null;
	}

	public Temperature postTemperature(TemperatureInsertion insert) throws SQLException {
		PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.temperature (temperature, sensor_id, log_time) values (?, (SELECT id FROM luft_sc.sensor WHERE token=?), ?);");
		stmt.setFloat(1, insert.getTemperature());
		stmt.setString(2, insert.getToken());
		if (insert.getTime() != null) stmt.setTimestamp(3, new Timestamp(insert.getTime().getTime()));
		else stmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
		Promise effect = new Promise();
		dbao.QueryUpdate(stmt, ((affected, set) -> { if (affected > 0) while (set.next()) effect.set(getFromSet(set)); }));
		return effect.isSet() ? (Temperature) effect.get() : null;
	}
}
