package org.vu.aso.next.nxt;

import lejos.nxt.Motor;

public class NxtSensorMonitor extends Thread {

	private static final int LOW_SPEED = 35;
	private static final int HIGH_SPEED = 100;
	private static final int INCREMENT = 2;

	private static final int SCANNER_MOTOR = 1;
	private static final int SCANNER_UP = 1;
	private static final int SCANNER_DOWN = 0;
	private static final int ANGLE = 2;

	MindstormsBrains parent;
	NxtSensorData data;

	NxtSensorMonitor(MindstormsBrains parent, NxtSensorData data) {
		this.data = data;
		this.parent = parent;
	}

	public void run() {
		for (int i = 0; i < data.sensorValues.length; i++) {
			if (parent.sensors[i].toBeMonitored()) {
				parent.sensors[i].on();
			}
		}

		while (true) {
			if (parent.scanScheduled()) {
				data.isScanning = "1";
				performScan();
				data.isScanning = "0";
				parent.descheduleScan();
			}

			for (int i = 0; i < data.sensorValues.length; i++) {
				if (parent.sensors[i].toBeMonitored()) {
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

	private void performScan() {
		StringBuffer result = new StringBuffer();

		int currentAngle = parent.scanFrom;
		int finalAngle = parent.scanTo;
		int numberOfMeasurements = (finalAngle - currentAngle) / INCREMENT;
		int[][] measurements = new int[2][numberOfMeasurements];
		boolean[] objectVisible = new boolean[numberOfMeasurements];
		boolean[] closestObjectVisible = new boolean[numberOfMeasurements];

		int closestBlockAngle = -1;
		int closestBlockDistance = 255;

		parent.sensors[SCANNER_DOWN].on();

		Motor motor = parent.motors[SCANNER_MOTOR];

		motor.setSpeed(HIGH_SPEED);
		motor.rotateTo(currentAngle);

		motor.setSpeed(LOW_SPEED);
		for (int i = 0; i < numberOfMeasurements; i++) {

			measurements[SCANNER_UP][i] = distance(SCANNER_UP);
			measurements[SCANNER_DOWN][i] = distance(SCANNER_DOWN);

			result.append(currentAngle);
			result.append('-');
			result.append(measurements[SCANNER_UP][i]);
			result.append('-');
			result.append(measurements[SCANNER_DOWN][i]);
			result.append(':');

			currentAngle += INCREMENT;
			motor.rotateTo(currentAngle);
		}

		parent.sensors[SCANNER_DOWN].off();

		motor.setSpeed(HIGH_SPEED);
		motor.rotateTo(0);

		for (int i = 0; i < numberOfMeasurements; i++) {
			objectVisible[i] = measurements[SCANNER_UP][i] >= measurements[SCANNER_DOWN][i] + 20 ? true
					: false;
		}
		int lowestDistance = lowest(measurements[SCANNER_DOWN], objectVisible);
		for (int i = 0; i < numberOfMeasurements; i++) {
			closestObjectVisible[i] = objectVisible[i]
			                                        && measurements[SCANNER_DOWN][i] - 5 <= lowestDistance ? true : false;
			                                        && measurements[SCANNER_DOWN][i] - 5 <= lowestDistance ? true : false;
					&& measurements[SCANNER_DOWN][i] - 5 <= lowestDistance ? true : false;
		}

		closestBlockAngle = angle(first(closestObjectVisible)) + angle(last(closestObjectVisible)) / 2;

		data.closestBlockAngle = Integer.toString(closestBlockAngle);
		data.closestBlockDistance = Integer.toString(closestBlockDistance);
		parent.scanResults = result.toString();
	}

	private int angle(int i) {
		return parent.scanFrom + i * INCREMENT;
	}

	private int first(boolean[] bools) {
		for (int i = 0; i < bools.length; i++) {
			if (bools[i]) {
				return i;
			}
		}
		return -1;
	}

	private int last(boolean[] bools) {
		int first = -1;
		for (int i = 0; i < bools.length; i++) {
			if (bools[i]) {
				first = i;
				break;
			}
		}
		if (first == -1) {
			return -1;
		} else {
			for (int i = first; i < bools.length; i++) {
				if (!bools[i]) {
					return i - 1;
				}
			}
			return bools.length - 1;
		}
	}

	private int lowest(int[] ints, boolean[] bools) {
		int lowest = -1;
		for (int i = 0; i < ints.length; i++) {
			if (bools[i] && ints[i] < lowest) {
				lowest = ints[i];
			}
		}
		return lowest;
	}

	private int distance(int scannerPort) {
		return Integer.parseInt(parent.sensors[scannerPort].getValue());
	}

}
