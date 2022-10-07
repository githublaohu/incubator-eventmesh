package org.apache.eventesh.connector.storage.jdbc.SQL;

public interface CloudEventSQLOperation {

	public String createCloudEventSQL(String table);

	public String insertCloudEventSQL(String table);

	public String updateCloudEventOffsetSQL(String table);

	public String updateCloudEventReplySQL(String table);

	public String selectCloudEventByReplySQL(String table, String idNum);

	public String locationEventSQL(String table);

	public String queryLocationEventSQL(String table);

	public String selectFastMessageSQL(String table);

	public String selectLastMessageSQL(String table);

	public String selectNoConsumptionMessageSQL(String table);

	public String selectAppointTimeMessageSQL(String table);
}
