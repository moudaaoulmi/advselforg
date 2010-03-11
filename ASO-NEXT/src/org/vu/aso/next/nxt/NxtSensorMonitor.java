package org.vu.aso.next.nxt;

public class NxtSensorMonitor extends Thread {

	MindstormsBrains parent;
	NxtSensorData data;

	NxtSensorMonitor(MindstormsBrains parent, NxtSensorData data) {
		this.data = data;
		this.parent = parent;
	}

	public void run() {
		for (int i = 0; i < data.sensorValues.length; i++) {
			if (parent.sensors[i] != null && parent.sensors[i].toBeMonitored()) {
				parent.sensors[i].on();
			}
		}

		while (true) {
			for (int i = 0; i < data.sensorValues.length; i++) {
				if (parent.sensors[i] != null && parent.sensors[i].toBeMonitored()) {
					data.sensorValues[i] = parent.sensors[i].getValue();
				}
			}
			for (int i = 0; i < data.tachoCounts.length; i++) {
				data.tachoCounts[i] = new Integer(parent.motors[i].getTachoCount()).toString();
			}
			data.travelDistance = new Integer((int) parent.pilot.getTravelDistance()).toString();
			data.isMoving = parent.pilot.isMoving() ? "1" : "0";
		}
	}
}
