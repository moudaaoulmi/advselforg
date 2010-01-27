package org.vu.advselforg.nxt;

public class NxtSensorMonitor extends Thread {

	MindstormsBrains parent;
	NxtSensorData data;

	NxtSensorMonitor(MindstormsBrains parent, NxtSensorData data) {
		this.data = data;
		this.parent = parent;
	}

	public void run() {
		while (true) {
			if (parent.scanScheduled()) {
				data.isScanning = "1";
				performScan();
				data.isScanning = "0";
				parent.descheduleScan();
			}

			for (int i = 0; i < data.sensorValues.length; i++) {
				data.sensorValues[i] = parent.sensors[i].getValue();
			}
			for (int i = 0; i < data.tachoCounts.length; i++) {
				data.tachoCounts[i] = new Integer(parent.motors[i].getTachoCount()).toString();
			}
			data.travelDistance = new Integer((int) parent.pilot.getTravelDistance()).toString();
			data.isMoving = parent.pilot.isMoving() ? "1" : "0";
		}
	}

	private void performScan() {

	}

}
