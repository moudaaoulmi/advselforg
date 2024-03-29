package org.vu.aso.next.nxt;

import org.vu.aso.next.common.ESensorType;

public class NxtSensorData {

	protected String[] sensorValues;
	protected ESensorType[] sensorTypes;
	protected String[] tachoCounts;
	protected String travelDistance;
	protected String isMoving;

	protected NxtSensorData() {
		sensorValues = new String[4];
		for (int i = 0; i < sensorValues.length; i++) {
			sensorValues[i] = "-1";
		}
		sensorTypes = new ESensorType[4];
		for (int i = 0; i < sensorTypes.length; i++) {
			sensorTypes[i] = ESensorType.NONE;
		}
		tachoCounts = new String[3];
		for (int i = 0; i < tachoCounts.length; i++) {
			tachoCounts[i] = "-1";
		}
		travelDistance = "0";
	}
}
