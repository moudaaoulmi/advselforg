package org.vu.aso.next.nxt;

import org.vu.aso.next.common.*;
import org.vu.aso.next.nxt.sensors.NxtLightSensor;
import org.vu.aso.next.nxt.sensors.NxtRFIDSensor;
import org.vu.aso.next.nxt.sensors.NxtSoundSensor;
import org.vu.aso.next.nxt.sensors.NxtTouchSensor;
import org.vu.aso.next.nxt.sensors.NxtUltrasonicSensor;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.robotics.navigation.TachoPilot;
import lejos.nxt.Sound;

import java.io.*;
import java.util.StringTokenizer;

public class MindstormsBrains {

	protected INxtSensor[] sensors;
	protected Motor[] motors;
	protected TachoPilot pilot;

	protected NxtSensorData data;
	private Thread monitor;

	private OutputStream out;
	private InputStream in;

	private boolean scanScheduled = false;
	protected int scanFrom;
	protected int scanTo;

	// Constructor and startup

	MindstormsBrains() {
		data = new NxtSensorData();
		monitor = new NxtSensorMonitor(this, data);

		waitForConnection();
	}

	private void waitForConnection() {
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		BTConnection conn = Bluetooth.waitForConnection();
		LCD.clear();
		LCD.drawString("Connected", 0, 0);

		out = conn.openOutputStream();
		in = conn.openInputStream();
	}

	private void run() throws Exception {
		/*
		 * while (true) { String[] message = getMessage();
		 * parseMessage(message); }
		 */
		for (int step = 0; step < 1000; step++) {
			String[] message = getMessage();
			parseMessage(message);
		}
		exit();
	}

	// Parsing stuff

	private void parseMessage(String[] message) throws Exception {
		if (message[0].charAt(0) != '\u0000') {
			int command = new Integer(message[0]);
			switch (command) {
			case NxtProtocol.EXIT:
				sendMessage(NxtProtocol.EXIT + ";");
				exit();
				break;
			case NxtProtocol.INIT:
				init(message);
				Thread.sleep(500); // Ensure there are sensor readings
				sendMessage(NxtProtocol.INIT + ";");
				break;
			case NxtProtocol.SENSOR_DATA:
				sendSensorData();
				break;
			case NxtProtocol.STOP:
				stop();
				sendMessage(NxtProtocol.STOP + ";");
				break;
			case NxtProtocol.FORWARD:
				forward(message);
				sendMessage(NxtProtocol.FORWARD + ";");
				break;
			case NxtProtocol.BACKWARD:
				backward(message);
				sendMessage(NxtProtocol.BACKWARD + ";");
				break;
			case NxtProtocol.TURN_LEFT:
				turnLeft(message);
				sendMessage(NxtProtocol.TURN_LEFT + ";");
				break;
			case NxtProtocol.TURN_RIGHT:
				turnRight(message);
				sendMessage(NxtProtocol.TURN_RIGHT + ";");
				break;
			case NxtProtocol.RESET_TRAVEL_DISTANCE:
				resetTravelDistance();
				sendMessage(NxtProtocol.RESET_TRAVEL_DISTANCE + ";");
				break;
			case NxtProtocol.PERFORM_SCAN:
				scheduleScan(message);
				sendMessage(NxtProtocol.PERFORM_SCAN + ";");
				break;
			default:
				LCD.drawString("No command!", 0, 0);
				break;
			}
		}
	}

	// Initialization stuff

	private void init(String[] config) {
		initMotors();
		initSensors(config);
		initPilot(config);

		monitor.start();
	}

	private void initMotors() {
		motors = new Motor[3];
		motors[0] = new Motor(MotorPort.A);
		motors[1] = new Motor(MotorPort.B);
		motors[2] = new Motor(MotorPort.C);
		
		for (Motor motor : motors) {
			motor.resetTachoCount();
		}
	}

	private void initSensors(String[] config) {
		sensors = new INxtSensor[4];
		initSensor(SensorPort.S1, 0, new Integer(config[1]));
		initSensor(SensorPort.S2, 1, new Integer(config[2]));
		initSensor(SensorPort.S3, 2, new Integer(config[3]));
		initSensor(SensorPort.S4, 3, new Integer(config[4]));
	}

	private void initSensor(SensorPort port, int portNumber, int type) {
		switch (type) {
		case NxtProtocol.NO_SENSOR:
			sensors[portNumber] = null;
			data.sensorTypes[portNumber] = ESensorType.NONE;
			break;
		case NxtProtocol.ULTRASONIC_SENSOR:
			sensors[portNumber] = new NxtUltrasonicSensor(port);
			data.sensorTypes[portNumber] = ESensorType.ULTRASONIC;
			break;
		case NxtProtocol.LIGHT_SENSOR:
			sensors[portNumber] = new NxtLightSensor(port);
			data.sensorTypes[portNumber] = ESensorType.LIGHT;
			break;
		case NxtProtocol.SOUND_SENSOR:
			sensors[portNumber] = new NxtSoundSensor(port);
			data.sensorTypes[portNumber] = ESensorType.SOUND;
			break;
		case NxtProtocol.TOUCH_SENSOR:
			sensors[portNumber] = new NxtTouchSensor(port);
			data.sensorTypes[portNumber] = ESensorType.TOUCH;
			break;
		case NxtProtocol.RFID_SENSOR:
			sensors[portNumber] = new NxtRFIDSensor(port);
			data.sensorTypes[portNumber] = ESensorType.RFID;
			break;
		}
	}

	private void initPilot(String[] config) {
		boolean motorReverse = new Integer(config[7]) == 1 ? true : false;
		pilot = new TachoPilot(new Float(config[8]), new Float(config[9]), motors[new Integer(
				config[5]) - 1], motors[new Integer(config[6]) - 1], motorReverse);
		pilot.setMoveSpeed(15);
		pilot.regulateSpeed(true);
	}

	// Commands

	private void exit() {
		monitor = null;
		System.exit(0);
	}

	private void sendSensorData() throws Exception {
		StringBuffer result = new StringBuffer();

		result.append(NxtProtocol.SENSOR_DATA);
		result.append(';');
		for (int i = 0; i < data.sensorValues.length; i++) {
			result.append(data.sensorValues[i]);
			result.append(';');
		}
		for (int i = 0; i < data.tachoCounts.length; i++) {
			result.append(data.tachoCounts[i]);
			result.append(';');
		}
		result.append(data.travelDistance);
		result.append(';');
		result.append(data.isMoving);
		result.append(';');
		result.append(data.isScanning);
		result.append(';');
		result.append(data.closestBlockAngle);
		result.append(';');
		result.append(data.closestBlockDistance);
		result.append(';');

		sendMessage(result.toString());
	}

	private void stop() {
		pilot.stop();
	}

	private void forward(String[] message) {
		float distance = new Float(message[1]);
		if (distance > 0) {
			pilot.travel(distance, true); // Non-blocking call
		}
	}

	private void backward(String[] message) {
		float distance = new Float(message[1]);
		if (distance > 0) {
			pilot.travel(distance * -1, true);
		}
	}

	private void turnLeft(String[] message) {
		float angle = new Float(message[1]);
		if (angle > 0) {
			pilot.rotate(angle);
		}
	}

	private void turnRight(String[] message) {
		float angle = new Float(message[1]);
		if (angle > 0) {
			pilot.rotate(angle * -1);
		}
	}

	private void resetTravelDistance() {
		pilot.reset();
	}

	private void scheduleScan(String[] message) {
		scanScheduled = true;
		this.scanFrom = Integer.parseInt(message[1]);
		this.scanTo = Integer.parseInt(message[2]);
	}

	protected void descheduleScan() {
		scanScheduled = false;
	}

	protected boolean scanScheduled() {
		return scanScheduled;
	}

	protected void beep() {
		Sound.playTone(1000, 10);
	}

	// Message-related stuff

	private void sendMessage(String message) throws IOException {
		out.write(message.getBytes(""));
		out.write(NxtProtocol.TERMINATION_BYTE);
		out.flush();
	}

	private String[] getMessage() throws Exception {
		String[] message = new String[NxtProtocol.MESSAGE_SIZE];
		byte[] buffer = new byte[NxtProtocol.MESSAGE_SIZE];

		in.read(buffer, 0, buffer.length);
		String s = new String(buffer);

		StringTokenizer st = new StringTokenizer(s, ";");

		int i = 0;
		while (st.hasMoreTokens()) {
			message[i] = st.nextToken();
			i++;
		}

		return message;
	}

	// Extra stuff

	public static void main(String[] args) {
		try {
			new MindstormsBrains().run();
		} catch (Exception e) {
			LCD.drawString(e.getMessage(), 0, 5);
			Button.waitForPress();
			System.exit(1);
		}
	}
}