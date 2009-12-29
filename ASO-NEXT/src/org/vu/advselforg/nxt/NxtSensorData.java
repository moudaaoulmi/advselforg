package org.vu.advselforg.nxt;

import org.vu.advselforg.common.ESensorType;

public class NxtSensorData {

	public String[] sensorValues;
	public ESensorType[] sensorTypes;
	public String[] tachoCounts;
	public String travelDistance;
	public String isMoving, isScanning;
	
	public NxtSensorData() {
		sensorValues = new String[4];
		for(int i = 0; i < sensorValues.length; i++) {
			sensorValues[i] = "-1";
		}
		sensorTypes = new ESensorType[4];
		for(int i = 0; i < sensorTypes.length; i++) {
			sensorTypes[i] = ESensorType.NONE;
		}
		tachoCounts = new String[3];
		for(int i = 0; i < tachoCounts.length; i++) {
			sensorValues[i] = "-1";
		}
		travelDistance = "0";
		isMoving = isScanning = "0";
	}
}
