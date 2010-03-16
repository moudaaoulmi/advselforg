	package org.vu.aso.next.pc;

import org.vu.aso.next.common.EObjectType;
import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.EMovingMode;

public class SensorData {

	private final static int MINIMUM = 0, MAXIMUM = 1;
	private final static int[] LIGHT_OBJECT_INTERVAL = { 42, 70 };
	private final static int[] DARK_OBJECT_INTERVAL = { 28, 38 };

	private int distanceUpperSonar;
	private int lightSensorValue;
	private boolean touchSensorPressed;
	private int motorATachoCount;
	private int motorBTachoCount;
	private int motorCTachoCount;
	private boolean isMoving;
	private EMovingMode lastCommand;
	private int travelDistance;

	public SensorData() {
		lastCommand = EMovingMode.FORWARD;
	}

	// At this moment, sensor types are fixed. Port 1 and 2 ultrasonic, port 3
	// light, port 4 touch.
	public void processMessage(String[] message) {
		distanceUpperSonar = Integer.parseInt(message[2]);

		lightSensorValue = Integer.parseInt(message[3]);
		touchSensorPressed = message[4].equals("1") ? true : false;

		motorATachoCount = Integer.parseInt(message[5]);
		motorBTachoCount = Integer.parseInt(message[6]);
		motorCTachoCount = Integer.parseInt(message[7]);

		travelDistance = Integer.parseInt(message[8]);

		isMoving = message[9].equals("1") ? true : false;
	}
	
	public void setLastCommand(EMovingMode mm) {
		lastCommand = mm;
	}

	public void setMoving(boolean moving) {
		isMoving = moving;
	}

	public int getDistanceUpperSonar() {
		return distanceUpperSonar;
	}

	public int getLightSensorValue() {
		return lightSensorValue;
	}

	public EObjectType getObjectType() {
		if (lightSensorValue >= LIGHT_OBJECT_INTERVAL[MINIMUM] && lightSensorValue <= LIGHT_OBJECT_INTERVAL[MAXIMUM]) {
			return EObjectType.LIGHT_OBJECT;
		} else if (lightSensorValue >= DARK_OBJECT_INTERVAL[MINIMUM]
				&& lightSensorValue <= DARK_OBJECT_INTERVAL[MAXIMUM]) {
			return EObjectType.DARK_OBJECT;
		} else {
			return EObjectType.NO_OBJECT;
		}
	}

	public boolean getTouchSensorPressed() {
		return touchSensorPressed;
	}

	public int getTachoMeterCount(EMotorPort port) {
		if (port == EMotorPort.A) {
			return motorATachoCount;
		} else if (port == EMotorPort.B) {
			return motorBTachoCount;
		} else if (port == EMotorPort.C) {
			return motorCTachoCount;
		} else {
			return -1;
		}
	}

	public boolean isMoving() {
		return isMoving;
	}

	public boolean isTurning() {
		return isMoving && lastCommand == EMovingMode.TURNING;
	}

	public boolean isDrivingForward() {
		return isMoving && lastCommand == EMovingMode.FORWARD;
	}

	public boolean isDrivingBackward() {
		return isMoving && lastCommand == EMovingMode.BACKWARD;
	}

	public EMovingMode lastCommand() {
		return lastCommand;
	}

	public int getTravelDistance() {
		return travelDistance;
	}

}
