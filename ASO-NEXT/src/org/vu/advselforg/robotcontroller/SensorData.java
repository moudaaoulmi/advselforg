package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.EMovingMode;

public class SensorData {

	private int _distanceUpperSonar;
	private int _distanceLowerSonar;
	private int _lightSensorValue;
	private boolean _touchSensorPressed;
	private int _motorATachoCount;
	private int _motorBTachoCount;
	private int _motorCTachoCount;
	private boolean _atDesiredMotorSpeed;
	private boolean _isMoving;
	private boolean _motorAIsScanning;
	private boolean _motorBIsScanning;
	private boolean _motorCIsScanning;
	private EMovingMode _lastCommand;
	private int _travelDistance;

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
		return true; // _atDesiredMotorSpeed;
	}

	public boolean isScanning(EMotorPort port) {
		if (port == EMotorPort.A) {
			return _motorAIsScanning;
		} else if (port == EMotorPort.B) {
			return _motorBIsScanning;
		} else {
			return _motorCIsScanning;
		}
	}

	public boolean isTurningOrMovingBackward() {
		if (_isMoving && _lastCommand != EMovingMode.FORWARD) {
			return true;
		} else {
			return false;
		}

		// if(_lastCommand == EMovingMode.BACKWARD || _lastCommand ==
		// EMovingMode.TURNING){
		// return true;
		// }else{
		// return false;
		// }
	}

	public EMovingMode lastCommand() {
		return _lastCommand;
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
		// message[2]
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

		_motorAIsScanning = message[10].equals("1") ? true : false;
		_motorBIsScanning = message[10].equals("1") ? true : false;
		_motorCIsScanning = message[10].equals("1") ? true : false;
	}
}
