package api.luft_api.database;

public class SharedDBAO {

	private static DataBaseAccessObject database_access_object = null;

	public static DataBaseAccessObject getInstance() {
		if (database_access_object == null) database_access_object = new DataBaseAccessObject();
		return database_access_object;
	}

	public static void setInstance(DataBaseAccessObject dbao) {
		database_access_object = dbao;
	}


}
