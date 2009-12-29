package org.vu.advselforg.nxt;
public class SensorData {

	protected String[] sensorValues;
	protected String[] tachoCounts;
	protected String travelDistance;
	protected String isMoving, isScanning;
	
	SensorData() {
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
