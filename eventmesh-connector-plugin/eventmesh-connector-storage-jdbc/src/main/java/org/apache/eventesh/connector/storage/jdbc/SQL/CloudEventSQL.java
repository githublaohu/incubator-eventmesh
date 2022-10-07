package org.apache.eventesh.connector.storage.jdbc.SQL;

import lombok.Data;

@Data
public class CloudEventSQL {

	private String createCloudEventSQL;

	private String insertCloudEventSQL;

	private String updateCloudEventOffsetSQL;

	private String updateCloudEventReplySQL;

	private String selectCloudEventByReplySQL;

	private String locationEventSQL;

	private String queryLocationEventSQL;

	private String selectFastMessageSQL;

	private String selectLastMessageSQL;

	private String selectNoConsumptionMessageSQL;

	private String selectAppointTimeMessageSQL;
}
