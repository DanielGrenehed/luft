package api.luft_api.humidity;

import api.luft_api.Promise;
import api.luft_api.database.DataBaseAccessObject;
import api.luft_api.database.SharedDBAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HumidityDAO {

	private DataBaseAccessObject dbao = SharedDBAO.getInstance();

	private Humidity getFromSet(ResultSet set) throws SQLException {
		return new Humidity(set.getFloat("humidity"), set.getTimestamp("log_time"));
	}

	private List<Humidity> queryList(PreparedStatement statement) throws SQLException {
		List<Humidity> result = new ArrayList<>();
		dbao.Query(statement, set -> { while (set.next()) result.add(getFromSet(set)); });
		return result;
	}

	public Humidity getLatestValue(int sensor_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT * FROM luft_sc.humidity WHERE sensor_id=? ORDER BY log_time DESC LIMIT 1;");
		stmt.setInt(1, sensor_id);
		Promise result = new Promise();
		dbao.Query(stmt, set -> { while (set.next()) result.set(getFromSet(set)); });
		return (Humidity) result.get();
	}
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

	/*
		Get 10 minute average for date
	*/
	public List<Humidity> getAveragesForDate(Date date, int sensor_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT AVG(humidity) AS humidity, log_time FROM (SELECT humidity, to_timestamp(FLOOR((EXTRACT(epoch from log_time))/600)*600) AS log_time FROM luft_sc.humidity WHERE sensor_id=? AND CAST(log_time AS DATE)=? GROUP BY log_time, humidity ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;");
		stmt.setInt(1,sensor_id);
		stmt.setString(2, dateFormat.format(date));
		return queryList(stmt);
	}

	public List<Humidity> getDailyAveragesForPeriod(int sensor_id, Date start, Date end) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT AVG(humidity) AS humidity, to_timestamp(to_char(log_time, 'HH24:MI'), 'HH24:MI') AS log_time FROM (SELECT humidity, to_timestamp(FLOOR((EXTRACT(epoch FROM log_time))/600)*600) AS log_time FROM luft_sc.humidity WHERE sensor_id=? AND CAST(log_time AS DATE) BETWEEN ? AND ? GROUP BY log_time, humidity ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;");
		stmt.setInt(1, sensor_id);
		stmt.setString(2, dateFormat.format(start));
		stmt.setString(3, dateFormat.format(end));
		return queryList(stmt);
	}

	/*
	* 	Get daily average humidity
	* */
	public List<Humidity> getAverageValues(int sensor_id) throws SQLException {
		PreparedStatement stmt = dbao.prepareStatement("SELECT sensor_id, CAST(log_time AS DATE), AVG(humidity) AS humidity FROM luft_sc.humidity WHERE sensor_id=? GROUP BY sensor_id, CAST(log_time AS DATE) ORDER BY log_time DESC;");
		stmt.setInt(1, sensor_id);
		return queryList(stmt);
	}

	public Humidity getLatestZoneValue(int zone_id) throws SQLException {
		Promise result = new Promise();
		PreparedStatement stmt = dbao.prepareStatement("SELECT * FROM luft_sc.humidity WHERE sensor_id IN (SELECT id FROM luft_sc.sensor WHERE zone_id=?) ORDER BY log_time DESC LIMIT 1;");
		stmt.setInt(1, zone_id);
		dbao.Query(stmt, set -> { while (set.next()) result.set(getFromSet(set)); });
		return (Humidity) result.get();
	}

	public Humidity postHumidity(float humidity) throws SQLException {
		PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.humidity (humidity, sensor_id) values (?, 1);");
		stmt.setFloat(1, humidity);
		Promise result = new Promise();
		dbao.QueryUpdate(stmt, ((affected, set) -> { if (affected > 0) while (set.next()) result.set(getFromSet(set)); }));
		return result.isSet() ? (Humidity) result.get() : null;
	}

	public Humidity postHumidity(HumidityInsertion insert) throws SQLException {
		PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.humidity (humidity, sensor_id, log_time) values (?, (SELECT id FROM luft_sc.sensor WHERE token=?), ?);");
		stmt.setFloat(1, insert.getHumidity());
		stmt.setString(2,insert.getToken());
		if (insert.getTime() != null) stmt.setTimestamp(3, new Timestamp(insert.getTime().getTime()));
		else stmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
		Promise result = new Promise();
		dbao.QueryUpdate(stmt, ((affected, set) -> {
			if (affected > 0) while (set.next()) result.set(getFromSet(set));

		}));
		return result.isSet() ? (Humidity) result.get() : null;
	}
}
