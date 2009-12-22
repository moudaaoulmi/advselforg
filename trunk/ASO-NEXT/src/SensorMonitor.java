public class SensorMonitor extends Thread {
	
	MindstormsBrains parent;
	SensorData data;
	
	SensorMonitor(MindstormsBrains parent, SensorData data) {
		this.data = data;
		this.parent = parent;
	}
	
	public void run() {
		while(true) {
			for(int i = 0; i < data.sensorValues.length; i++) {
				data.sensorValues[i] = parent.sensors[i].getValue();
			}
			for(int i = 0; i < data.tachoCounts.length; i++) {
				data.tachoCounts[i] = new Integer(parent.motors[i].getTachoCount()).toString();
			}
			data.travelDistance = new Float(parent.pilot.getTravelDistance()).toString();
			data.isMoving = parent.pilot.isMoving() ? "1" : "0";
			data.isScanning = "0";
		}
	}

}
