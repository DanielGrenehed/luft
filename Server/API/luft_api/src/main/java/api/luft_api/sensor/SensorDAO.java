package api.luft_api.sensor;

import api.luft_api.Promise;
import api.luft_api.database.DataBaseAccessObject;
import api.luft_api.database.SharedDBAO;
import api.luft_api.humidity.Humidity;
import api.luft_api.humidity.HumidityDispatcher;
import api.luft_api.temperature.Temperature;
import api.luft_api.temperature.TemperatureDispatcher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SensorDAO {

	private DataBaseAccessObject dbao = SharedDBAO.getInstance();

	private Sensor getFromSet(ResultSet set) throws SQLException {
		return new Sensor(set.getInt("id"), set.getString("token"), set.getString("sensor_name"));
	}
	public List<Sensor> getSensorsInZone(int zone_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT id, token, sensor_name FROM luft_sc.sensor WHERE zone_id=?;");
		stmt.setInt(1, zone_id);
		List<Sensor> result = new ArrayList<>();
		dbao.Query(stmt, set -> { while (set.next()) result.add(getFromSet(set)); });
		return result;
	}

	public SensorID getSensorIdFromToken(String token) throws SQLException {
		PreparedStatement statement = dbao.prepareStatement("SELECT id FROM luft_sc.sensor WHERE token=?;");
		statement.setString(1, token);
		SensorID result = new SensorID();
		dbao.Query(statement, set -> {
			while (set.next()) result.setSensor_id(set.getInt("id"));
		});
		return result;
	}

	public boolean postReading(ReadingInsertion insert) throws SQLException {
		System.out.println("Sensor token:" + insert.getToken());
		// post humidity
		Promise humidity_res = new Promise();
		Promise temperature_res = new Promise();
		try {
			PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.humidity (humidity, sensor_id, log_time) values (?, (SELECT id FROM luft_sc.sensor WHERE sensor.token=?), ?);");
			stmt.setFloat(1, insert.getHumidity());
			stmt.setString(2, insert.getToken());
			if (insert.getTime() != null) stmt.setTimestamp(3, new Timestamp(insert.getTime().getTime()));
			else stmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
			dbao.QueryUpdate(stmt, ((affected, set) -> {
				if (affected > 0) while (set.next()) humidity_res.set(new Humidity(set.getFloat("humidity"), set.getTimestamp("log_time")));
			}));
		} catch (SQLException e) {
			System.out.print("h.");
		}
		try {
			// post temperature
			PreparedStatement temp_stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.temperature (temperature, sensor_id, log_time) values (?, (SELECT id FROM luft_sc.sensor WHERE sensor.token=?), ?);");
			temp_stmt.setFloat(1, insert.getTemperature());
			temp_stmt.setString(2, insert.getToken());
			if (insert.getTime() != null) temp_stmt.setTimestamp(3, new Timestamp(insert.getTime().getTime()));
			else temp_stmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
			dbao.QueryUpdate(temp_stmt, ((affected, set) -> {
				if (affected > 0) while (set.next()) temperature_res.set(new Temperature(set.getFloat("temperature"), set.getTimestamp("log_time")));
			}));
		} catch (SQLException e) {
			System.out.print("t.");
		}

		SensorID sid = getSensorIdFromToken(insert.getToken());
		if (humidity_res.isSet()) {
			// post humidity to ws
			HumidityDispatcher.getInstance().dispatchHumidity((Humidity) humidity_res.get(), sid);
		}
		if (temperature_res.isSet()) {
			// post temperature to ws
			TemperatureDispatcher.getInstance().dispatchTemperature((Temperature) temperature_res.get(), sid);
		}

		return humidity_res.isSet() && temperature_res.isSet();
	}
}
