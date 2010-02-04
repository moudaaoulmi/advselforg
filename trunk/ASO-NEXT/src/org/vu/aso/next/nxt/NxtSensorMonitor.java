package org.vu.aso.next.nxt;

import lejos.nxt.Motor;

public class NxtSensorMonitor extends Thread {

	private static final int LOW_SPEED = 35;
	private static final int HIGH_SPEED = 100;
	private static final int SCANNER_MOTOR = 1;
	private static final int SCANNER_UP = 1;
	private static final int SCANNER_DOWN = 0;
	
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
		int moveTo = parent.scanFrom;
		int closestBlockAngle = -1;
		int closestBlockDistance = 255;
		
		Motor motor = parent.motors[SCANNER_MOTOR];
		
		motor.setSpeed(HIGH_SPEED);
		motor.rotateTo(moveTo);
		
		motor.setSpeed(LOW_SPEED);
		while (moveTo != parent.scanTo) {
			if (distance(SCANNER_UP) > distance(SCANNER_DOWN) + 20 && distance(SCANNER_DOWN) < closestBlockDistance) {
				closestBlockAngle = moveTo;
				closestBlockDistance = distance(SCANNER_DOWN);
			}
			moveTo -= 5;
			motor.rotateTo(moveTo);
		}
		
		motor.setSpeed(HIGH_SPEED);
		motor.rotateTo(0);
		
		data.closestBlockAngle = Integer.toString(closestBlockAngle);
		data.closestBlockDistance = Integer.toString(closestBlockDistance);
	}
	
	private int distance(int scannerPort) {
		return Integer.parseInt(parent.sensors[scannerPort].getValue());
	}

}