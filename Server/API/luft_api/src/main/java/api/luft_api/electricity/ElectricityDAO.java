package api.luft_api.electricity;

import api.luft_api.Promise;
import api.luft_api.database.DataBaseAccessObject;
import api.luft_api.database.SharedDBAO;
import api.luft_api.humidity.Humidity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ElectricityDAO {

	private DataBaseAccessObject dbao = SharedDBAO.getInstance();

	private Electricity getFromSet(ResultSet set) throws SQLException {
		return new Electricity(set.getFloat("price"), set.getTimestamp("log_time"));
	}
	public Electricity getLatestPrice() throws SQLException {
		Promise result = new Promise();
		dbao.Query("SELECT price, log_time FROM luft_sc.electricity_price ORDER BY log_time DESC LIMIT 1;", set -> {
			while (set.next()) result.set(getFromSet(set));
		});
		return (Electricity) result.get();
	}

	public Electricity postElectricity(Electricity price) throws SQLException {
		PreparedStatement stmt = dbao.prepareUpdateStatement("INSERT INTO luft_sc.electricity_price (price, log_time) values (?, ?);");
		stmt.setFloat(1, price.getPrice());
		if (price.getTime() != null) stmt.setTimestamp(2, new Timestamp(price.getTime().getTime()));
		else stmt.setTimestamp(2, new Timestamp((new Date()).getTime()));
		Promise result = new Promise();
		dbao.QueryUpdate(stmt, ((affected, set) -> {
			if (affected > 0) while (set.next()) result.set(getFromSet(set));
		}));
		return result.isSet() ? (Electricity) result.get() : null;
	}
}
