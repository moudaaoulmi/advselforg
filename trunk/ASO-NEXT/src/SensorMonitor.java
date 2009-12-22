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
		}
	}

}
