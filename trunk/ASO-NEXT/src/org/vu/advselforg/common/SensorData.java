package org.vu.advselforg.common;

public class SensorData {

	public String[] sensorValues;
	public ESensorType[] sensorTypes;
	public String[] tachoCounts;
	public String travelDistance;
	public String isMoving, isScanning;
	
	public SensorData() {
		sensorValues = new String[4];
		for(int i = 0; i < sensorValues.length; i++) {
			sensorValues[i] = "-1";
		}
		tachoCounts = new String[3];
		for(int i = 0; i < tachoCounts.length; i++) {
			sensorValues[i] = "-1";
		}
		travelDistance = "0";
		isMoving = isScanning = "0";
	}
}
