package org.vu.advselforg.robotcontroller;

import java.io.IOException;
import java.util.ArrayList;

import org.vu.advselforg.common.EDistanceMode;
import org.vu.advselforg.common.EMovingMode;
import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;

import lejos.nxt.addon.RFIDSensor;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteSensorPort;
import lejos.robotics.navigation.TachoPilot;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorConstants;
import lejos.nxt.SoundSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class NxtController implements RobotController {

	private NXTCommand nxtCommand;
	private NXTConnector conn;
	private NXTInfo nxt;
	private NXTComm nxtComm;
	private ArrayList<UltrasonicSensor> ultrasonicsensors;
	private ArrayList<LightSensor> lightsensors;
	private ArrayList<SoundSensor> soundsensors;
	private ArrayList<TouchSensor> touchsensors;
	private ArrayList<RFIDSensor> rfidsensors;
	private TachoPilot pilot;
	private RemoteSensorPort[] sensors;
	private RemoteMotor[] motors;
	private EMovingMode lastCommand;
	private boolean isScanning = false;
	
	private static final int DESIRED_SPEED_MARGIN = 1;

	public NxtController(String robotName, EMotorPort leftMotorPort,
			EMotorPort rightMotorPort, boolean motorReverse, ESensorType type1,
			ESensorType type2, ESensorType type3, ESensorType type4) {

		// Create a connection and search for robots named [robotName]
		conn = new NXTConnector();
		nxt = search(robotName);

		// If a NXT is found, connect to it
		if (nxt != null) {
			try {
				nxtComm = NXTCommFactory.createNXTComm(nxt.protocol);
				nxtComm.open(nxt);
				System.out.println("Connected to " + nxt.name);
			} catch (NXTCommException e) {
				e.printStackTrace();
			}
		} else {
			throw new Error("Can't find " + robotName);
		}

		// Create a command object for the NXT
		nxtCommand = new NXTCommand();
		nxtCommand.setNXTComm(nxtComm);

		// Create 3 remote motors
		motors = new RemoteMotor[4];
		for (int i = 0; i < 3; i++) {
			motors[i + 1] = new RemoteMotor(nxtCommand, i);
			motors[i + 1].resetTachoCount();
		}

		// Create a pilot
		if (leftMotorPort != EMotorPort.NONE && rightMotorPort != EMotorPort.NONE) {
			pilot = new TachoPilot(5.4f, 15.1f, motors[leftMotorPort.ordinal()],
					motors[rightMotorPort.ordinal()], motorReverse);
			pilot.setMoveSpeed(15);
			pilot.regulateSpeed(true);
		}

		// Create 4 remote sensor ports
		sensors = new RemoteSensorPort[5];
		for (int i = 0; i < 4; i++) {
			sensors[i + 1] = new RemoteSensorPort(nxtCommand, i);
		}

		// Initialize the sensor arraylists
		ultrasonicsensors = new ArrayList<UltrasonicSensor>();
		lightsensors = new ArrayList<LightSensor>();
		soundsensors = new ArrayList<SoundSensor>();
		touchsensors = new ArrayList<TouchSensor>();
		rfidsensors = new ArrayList<RFIDSensor>();

		// Set the types of the sensors
		initSensor(sensors[1], type1);
		initSensor(sensors[2], type2);
		initSensor(sensors[3], type3);
		initSensor(sensors[4], type4);

	}

	/* Set the type of sensor [port] to [type] */
	private void initSensor(RemoteSensorPort port, ESensorType type) {
		switch (type) {
		case NONE:
			port.setType(SensorConstants.TYPE_NO_SENSOR);
			break;
		case ULTRASONIC:
			ultrasonicsensors.add(new UltrasonicSensor(port));
			break;
		case LIGHT:
			lightsensors.add(new LightSensor(port, true));
			break;
		case SOUND:
			soundsensors.add(new SoundSensor(port));
			break;
		case TOUCH:
			touchsensors.add(new TouchSensor(port));
			break;
		case RFID:
			rfidsensors.add(new RFIDSensor(port));
			break;
		}
	}

	/* Close the connection */
	protected void finalize() throws Throwable {
		try {
			if (nxtCommand == null) {
				return;
			} else {
				nxtCommand.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			super.finalize();
		}
	}

	/* Find NXT's with a given name */
	private NXTInfo search(String robotName) {
		NXTInfo[] nxts;
		nxts = conn.search(robotName, null, NXTCommFactory.BLUETOOTH);
		if (nxts.length > 0) {
			return nxts[0];
		} else {
			return null;
		}
	}

	/* Move the robot [distance] cm forward */
	public void moveForward(float distance, boolean immediateReturn) {
		if (distance <= 0) {
			return;
		}
		if (pilot != null) {
			lastCommand = EMovingMode.FORWARD;
			pilot.travel(distance, immediateReturn);
		}
	}

	/* Move the robot [distance] cm backward */
	public void moveBackward(float distance, boolean immediateReturn) {
		if (distance <= 0) {
			return;
		}
		if (pilot != null) {
			lastCommand = EMovingMode.BACKWARD;
			pilot.travel(-distance, immediateReturn);
		}
	}

	/* Rotate the robot [angle] degrees to the left */
	public void turnLeft(float angle, boolean immediateReturn) {
		if (pilot != null) {
			lastCommand = EMovingMode.TURNING;
			pilot.rotate(angle, immediateReturn);
		}
	}

	/* Rotate the robot [angle] degrees to the right */
	public void turnRight(float angle, boolean immediateReturn) {
		if (pilot != null) {
			lastCommand = EMovingMode.TURNING;
			pilot.rotate(-angle, immediateReturn);
		}
	}
	
	public void stop() {
		if (pilot != null) {
			pilot.stop();
		}
	}

	/* Get the distance of a ultrasonic sensor */
	public int getDistance(int sensorIndex, EDistanceMode mode) {
		int data[] = new int[3];
		int result;
		//String str = "";
		if (ultrasonicsensors.size() >= sensorIndex + 1) {
			try {
				for (int i = 0; i < data.length; i++) {
					Thread.sleep(10);
					data[i] = ultrasonicsensors.get(sensorIndex).getDistance();
				}
				/*for (int i = 0; i < data.length; i++) {
					str += data[i] + " ";
				}*/
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (mode == EDistanceMode.LOWEST) {
				result = 255;
				for (int i = 0; i < data.length; i++) {
					result = data[i] < result ? data[i] : result;
				}
			} else {
				result = -1;
				for (int i = 0; i < data.length; i++) {
					result = data[i] > result && data[i] != 255 ? data[i] : result;
				}
				if (result == -1) result = 255;
			}
			//System.out.println(str);
			return result;
		} else {
			return -1;
		}
	}

	/* Get the distance of the first ultrasonic sensor */
	public int getDistance(EDistanceMode mode) {
		return getDistance(0, mode);
	}

	/* Get the light value of a light sensor */
	public int getLightValue(int sensorIndex) {
		if (!lightsensors.isEmpty()) {
			return lightsensors.get(sensorIndex).getLightValue();
		} else {
			return -1;
		}
	}

	/* Get the light value of the first light sensor */
	public int getLightValue() {
		return getLightValue(0);
	}

	/* Get the sound level of a sound sensor */
	public int getSoundLevel(int sensorIndex) {
		if (!soundsensors.isEmpty()) {
			return soundsensors.get(sensorIndex).readValue();
		} else {
			return -1;
		}
	}

	/* Get the sound level of the first sound sensor */
	public int getSoundLevel() {
		return getSoundLevel(0);
	}

	/* Get the state of a touch sensor */
	public boolean getTouchSensorPressed(int sensorIndex) {
		if (!touchsensors.isEmpty()) {
			return touchsensors.get(sensorIndex).isPressed();
		} else {
			return false;
		}
	}

	/* Get the state of the first touch sensor */
	public boolean getTouchSensorPressed() {
		return getTouchSensorPressed(0);
	}

	/* Read a transponder with a RFID sensor */
	public long getRFID(int sensorIndex) {
		if (!rfidsensors.isEmpty()) {
			return rfidsensors.get(sensorIndex).readTransponderAsLong(false);
		} else {
			return -1;
		}
	}

	/* Read a transponder with the first RFID sensor */
	public long getRFID() {
		return getRFID(0);
	}

	public boolean atDesiredMotorSpeed() {
		System.out.println("Right:  " + motors[1].getRotationSpeed());
		System.out.println("Left:   " + motors[3].getRotationSpeed());
		System.out.println("Actual: " + pilot.getMoveSpeed());
		
		return pilot.getRightActualSpeed() >= pilot.getMoveSpeed() - DESIRED_SPEED_MARGIN &&
			pilot.getLeftActualSpeed() >= pilot.getMoveSpeed() - DESIRED_SPEED_MARGIN;
	}

	public int getTachoMeterCount(EMotorPort port) {
		return motors[port.ordinal()].getTachoCount();
	}

	public int getTravelDistance() {
		return (int) pilot.getTravelDistance();
	}

	public boolean isTurningOrDrivingBack() {
		if(lastCommand != EMovingMode.FORWARD){
			return pilot.isMoving();
		}else{return false;}
	}
	
	public EMovingMode lastCommand() {
		return lastCommand;
	}

	public boolean isScanning(EMotorPort port) {
		if (!motors[port.ordinal()].isMoving()) {
			isScanning = false;
		}
		return isScanning && motors[port.ordinal()].isMoving();
	}

	public void calibrateTurret(EMotorPort port) {
		int tachoDrift = motors[port.ordinal()].getTachoCount() * -1;
		if (Math.abs(tachoDrift) > 2) {
			motors[port.ordinal()].rotateTo(tachoDrift);
		}
	}

	public void performScan(EMotorPort port, int fromAngle, int toAngle, int speed) {
		motors[port.ordinal()].setSpeed(speed);
		motors[port.ordinal()].rotateTo(fromAngle, false);
		isScanning = true;
		motors[port.ordinal()].rotateTo(toAngle, true);
	}
	
	public void resetTravelDistance(){
		pilot.reset();
	}
}
