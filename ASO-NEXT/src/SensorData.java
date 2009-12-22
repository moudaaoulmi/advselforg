public class SensorData {

	protected String[] sensorValues;
	protected int tachoCountA, tachoCountB, tachoCountC;
	protected boolean isMoving, isScanning;
	
	SensorData() {
		sensorValues = new String[4];
		for(int i = 0; i < sensorValues.length; i++) {
			sensorValues[i] = "-1";
		}
		tachoCountA = tachoCountB = tachoCountC = -1;
		isMoving = isScanning = false;
	}
}
