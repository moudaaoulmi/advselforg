package org.vu.aso.next.pc;

import org.vu.aso.next.common.ELightSensorValue;
import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.EMovingMode;

public class SensorData {

	private final static int MINIMUM = 0, MAXIMUM = 1;
	private final static int[] LIGHT_OBJECT_INTERVAL = { 45, 70 };
	private final static int[] DARK_OBJECT_INTERVAL = { 30, 37 };

	private int _distanceUpperSonar;
	private int _distanceLowerSonar;
	private int _lightSensorValue;
	private boolean _touchSensorPressed;
	private int _motorATachoCount;
	private int _motorBTachoCount;
	private int _motorCTachoCount;
	private boolean _atDesiredMotorSpeed = true;
	private boolean _isMoving;
	private boolean _isScanning;
	private EMovingMode _lastCommand;
	private int _travelDistance;
	private int _closestblockAngle;
	private int _closestBlockDistance;

	public SensorData() {
		_lastCommand = EMovingMode.FORWARD;
	}

	public void setLastCommand(EMovingMode mm) {
		_lastCommand = mm;
	}

	public int getDistanceUpperSonar() {
		return _distanceUpperSonar;
	}

	public int getDistanceLowerSonar() {
		return _distanceLowerSonar;
	}

	public int getLightSensorValue() {
		return _lightSensorValue;
	}

	public ELightSensorValue getObjectType() {
		if (_lightSensorValue >= LIGHT_OBJECT_INTERVAL[MINIMUM] && _lightSensorValue <= LIGHT_OBJECT_INTERVAL[MAXIMUM]) {
			return ELightSensorValue.LIGHT_OBJECT;
		} else if (_lightSensorValue >= DARK_OBJECT_INTERVAL[MINIMUM] && _lightSensorValue <= DARK_OBJECT_INTERVAL[MAXIMUM]) {
			return ELightSensorValue.DARK_OBJECT;
		} else {
			return ELightSensorValue.NO_OBJECT;
		}
	}

	public boolean getTouchSensorPressed() {
		return _touchSensorPressed;
	}

	public int getTachoMeterCount(EMotorPort port) {
		if (port == EMotorPort.A) {
			return _motorATachoCount;
		} else if (port == EMotorPort.B) {
			return _motorBTachoCount;
		} else {
			return _motorCTachoCount;
		}
	}

	public boolean atDesiredMotorSpeed() {
		return _atDesiredMotorSpeed;
	}

	public boolean isMoving() {
		return _isMoving;
	}
	
	public boolean isScanning() {

		return _isScanning;
	}

	public boolean isTurningOrMovingBackward() {
		if (_isMoving && _lastCommand != EMovingMode.FORWARD) {
			return true;
		} else {
			return false;
		}
	}

	public EMovingMode lastCommand() {
		return _lastCommand;
	}

	public int getClosestblockAngle() {
		return _closestblockAngle;
	}

	public int getClosestblockDistance() {
		return _closestBlockDistance;
	}

	public int getTravelDistance() {
		return _travelDistance;
	}

	// At this moment I expect fixed sensortypes. Port 1 and 2 ultra sonic, port
	// 3 licht, port 4 touch.
	public void processMessage(String[] message) {
		_distanceLowerSonar = Integer.parseInt(message[1]);
		_distanceUpperSonar = Integer.parseInt(message[2]);

		_lightSensorValue = Integer.parseInt(message[3]);
		if (message[4].equals("1")) {
			_touchSensorPressed = true;
		} else {
			_touchSensorPressed = false;
		}

		_motorATachoCount = Integer.parseInt(message[5]);
		_motorBTachoCount = Integer.parseInt(message[6]);
		_motorCTachoCount = Integer.parseInt(message[7]);

		_travelDistance = Integer.parseInt(message[8]);

		_isMoving = message[9].equals("1") ? true : false;

		_isScanning = message[10].equals("1") ? true : false;

		_closestblockAngle = Integer.parseInt(message[11]);
		_closestBlockDistance = Integer.parseInt(message[12]);
	}
}
