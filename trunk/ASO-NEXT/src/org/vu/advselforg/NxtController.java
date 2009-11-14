package org.vu.advselforg;

import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.addon.RFIDSensor;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteSensorPort;
import lejos.robotics.navigation.TachoPilot;
import lejos.nxt.LightSensor;
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

	public NxtController(String robotName, int leftMotorPort,
			int rightMotorPort, boolean motorReverse, SensorType type1,
			SensorType type2, SensorType type3, SensorType type4) {
		conn = new NXTConnector();
		nxt = search(robotName);

		try {
			nxtComm = NXTCommFactory.createNXTComm(nxt.protocol);
			nxtComm.open(nxt);
			System.out.println("Connected to " + nxt.name);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}

		nxtCommand = new NXTCommand();
		nxtCommand.setNXTComm(nxtComm);

		motors = new RemoteMotor[4];
		for (int i = 0; i < 3; i++) {
			motors[i + 1] = new RemoteMotor(nxtCommand, i);
		}

		if (leftMotorPort != 0 && rightMotorPort != 0) {
			pilot = new TachoPilot(1.0f, 1, 0f, motors[leftMotorPort],
					motors[rightMotorPort], motorReverse);
		}

		sensors = new RemoteSensorPort[5];
		for (int i = 0; i < 4; i++) {
			sensors[i + 1] = new RemoteSensorPort(nxtCommand, i);
		}

		ultrasonicsensors = new ArrayList<UltrasonicSensor>();
		lightsensors = new ArrayList<LightSensor>();
		soundsensors = new ArrayList<SoundSensor>();
		touchsensors = new ArrayList<TouchSensor>();
		rfidsensors = new ArrayList<RFIDSensor>();

		initSensor(sensors[1], type1);
		initSensor(sensors[2], type2);
		initSensor(sensors[3], type3);
		initSensor(sensors[4], type4);

	}

	private void initSensor(RemoteSensorPort port, SensorType type) {
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
	public void moveForward(int distance, boolean immediateReturn) {
		if (pilot != null) {
			pilot.travel(distance, immediateReturn);
		}
	}

	public void moveBackward(int distance, boolean immediateReturn) {
		if (pilot != null) {
			pilot.travel(-distance, immediateReturn);
		}
	}

	public void turnLeft(int degrees, boolean immediateReturn) {
		if (pilot != null) {
			pilot.rotate((float) -degrees, immediateReturn);
		}
	}

	public void turnRight(int degrees, boolean immediateReturn) {
		if (pilot != null) {
			pilot.rotate((float) degrees, immediateReturn);
		}
	}

	public int getDistance(int sensorIndex) {
		if (!ultrasonicsensors.isEmpty()) {
			return ultrasonicsensors.get(sensorIndex).getDistance();
		} else {
			return -1;
		}
	}

	public int getDistance() {
		return getDistance(0);
	}

	public int getLightValue(int sensorIndex) {
		if (!lightsensors.isEmpty()) {
			return lightsensors.get(sensorIndex).getLightValue();
		} else {
			return -1;
		}
	}

	public int getLightValue() {
		return getLightValue(0);
	}

	public int getSoundLevel(int sensorIndex) {
		if (!soundsensors.isEmpty()) {
			return soundsensors.get(sensorIndex).readValue();
		} else {
			return -1;
		}
	}

	public int getSoundLevel() {
		return getSoundLevel(0);
	}

	public boolean getTouchSensorPressed(int sensorIndex) {
		if (!touchsensors.isEmpty()) {
			return touchsensors.get(sensorIndex).isPressed();
		} else {
			return false;
		}
	}

	public boolean getTouchSensorPressed() {
		return getTouchSensorPressed(0);
	}

	public long getRFID(int sensorIndex) {
		if (!rfidsensors.isEmpty()) {
			return rfidsensors.get(sensorIndex).readTransponderAsLong(false);
		} else {
			return -1;
		}
	}

	public long getRFID() {
		return getRFID(0);
	}
}
