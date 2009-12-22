import lejos.nxt.*;
import lejos.nxt.addon.RFIDSensor;
import lejos.nxt.comm.*;
import lejos.robotics.navigation.TachoPilot;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MindstormsBrains {
	
	private ArrayList<UltrasonicSensor> ultrasonicSensors;
	private ArrayList<LightSensor> lightSensors;
	private ArrayList<SoundSensor> soundSensors;
	private ArrayList<TouchSensor> touchSensors;
	private ArrayList<RFIDSensor> rfidSensors;
	
	private OutputStream out;
	private InputStream in;
	
	private Motor[] motors;
	private TachoPilot pilot;
	
	// Construction and startup
	
	MindstormsBrains() {
		ultrasonicSensors = new ArrayList<UltrasonicSensor>();
		lightSensors = new ArrayList<LightSensor>();
		soundSensors = new ArrayList<SoundSensor>();
		touchSensors = new ArrayList<TouchSensor>();
		rfidSensors = new ArrayList<RFIDSensor>();
		
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
		while (true) {
			String[] message = getMessage();
			parseMessage(message);
		}
	}
	
	// Parsing stuff
	
	private void parseMessage(String[] message) throws Exception {
		if (message[0].charAt(0) != '\u0000') {
			int command = new Integer(message[0]);
			switch (command) {
				case NxtProtocol.EXIT:
					exit();
					break;
				case NxtProtocol.INIT:
					init(message);
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
					performScan();
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
	}

	private void initMotors() {
		motors = new Motor[4];
		motors[1] = new Motor(MotorPort.A);
		motors[2] = new Motor(MotorPort.B);
		motors[3] = new Motor(MotorPort.C);
	}
	
	private void initSensors(String[] config) {
		initSensor(SensorPort.S1, new Integer(config[1]));
		initSensor(SensorPort.S2, new Integer(config[2]));
		initSensor(SensorPort.S3, new Integer(config[3]));
		initSensor(SensorPort.S4, new Integer(config[4]));
	}
	
	private void initPilot(String[] config) {
		boolean motorReverse = new Integer(config[9]) == 1 ? true : false;
		pilot = new TachoPilot(new Float(config[7]), new Float(config[8]),
				motors[new Integer(config[5])], motors[new Integer(config[6])], motorReverse);
		pilot.setMoveSpeed(15);
		pilot.regulateSpeed(true);
	}
	
	private void initSensor(SensorPort port, int type) {
		switch (type) {
			case 0:
				break;
			case 1:
				ultrasonicSensors.add(new UltrasonicSensor(port));
			case 2:
				touchSensors.add(new TouchSensor(port));
				break;
			case 3:
				lightSensors.add(new LightSensor(port, true));
				break;
			case 4:
				soundSensors.add(new SoundSensor(port));
				break;
			case 5:
				rfidSensors.add(new RFIDSensor(port));
				break;
		}
	}
	
	// Commands
	
	private void exit() {
		System.exit(0);
	}
	
	private void sendSensorData() {
		// Todo
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
			pilot.travel(distance * -1);
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
	
	private void performScan() {
		// Todo
	}
	
	// Message-related stuff
	
	private void sendMessage(String message) throws IOException {
		out.write(message.getBytes(""));
		out.write(-1);
		out.flush();
	}

	private String[] getMessage() throws Exception {
		String[] message = new String[20];
		byte[] buffer = new byte[50];

		in.read(buffer, 0, buffer.length);
		String s = new String(buffer);

		StringTokenizer st = new StringTokenizer(s, ";");

		int i = 0;
		while(st.hasMoreTokens()) {
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
