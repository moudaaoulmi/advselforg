package org.vu.advselforg;

import java.io.IOException;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteSensorPort;
import lejos.pc.comm.*;
import lejos.robotics.navigation.TachoPilot;
import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;

public class NxtController implements RobotController {

	private NXTCommand nxtCommand;
	private NXTConnector conn;
	private NXTInfo nxt;
	private NXTComm nxtComm;
	private UltrasonicSensor ultrasonicsensor;
	private LightSensor lightsensor;
	private TachoPilot pilot;
	private RemoteSensorPort[] sensors;
	private RemoteMotor[] motors;

	public NxtController(String robotName, int leftMotorPort,
			int rightMotorPort, boolean motorReverse, int ultrasonicSensorPort,
			int lightSensorPort) {
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

		sensors = new RemoteSensorPort[5];
		for (int i = 0; i < 4; i++) {
			sensors[i + 1] = new RemoteSensorPort(nxtCommand, i);
		}

		motors = new RemoteMotor[4];
		for (int i = 0; i < 3; i++) {
			motors[i + 1] = new RemoteMotor(nxtCommand, i);
		}

		if (ultrasonicSensorPort != 0) {
			ultrasonicsensor = new UltrasonicSensor(
					sensors[ultrasonicSensorPort]);
		}

		if (lightSensorPort != 0) {
			lightsensor = new LightSensor(sensors[lightSensorPort]);
		}

		if (leftMotorPort != 0 && rightMotorPort != 0) {
			pilot = new TachoPilot(1.0f, 1, 0f, motors[leftMotorPort],
					motors[rightMotorPort], motorReverse);
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

	public int getDistance() {
		if (ultrasonicsensor != null) {
			return ultrasonicsensor.getDistance();
		} else {
			return -1;
		}
	}

	public int getLightValue() {
		if (lightsensor != null) {
			return lightsensor.getLightValue();
		} else {
			return -1;
		}
	}

	public int getTouchSensorPressed() {
		return 0;
	}

	public int getRFID() {
		return 0;
	}

}
