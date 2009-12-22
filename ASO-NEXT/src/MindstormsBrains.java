import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.robotics.navigation.TachoPilot;

import java.io.*;
import java.util.StringTokenizer;

public class MindstormsBrains {
	
	protected Sensor[] sensors;
	protected Motor[] motors;
	protected TachoPilot pilot;

	protected SensorData data;
	private Thread monitor;
	
	private OutputStream out;
	private InputStream in;

	// Construction and startup
	
	MindstormsBrains() {
		data = new SensorData();
		monitor = new SensorMonitor(this, data);
		
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
		/*while (true) {
			String[] message = getMessage();
			parseMessage(message);
		}*/
		for (int step = 0; step < 1000; step++) {
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
		
		monitor.start();
	}

	private void initMotors() {
		motors = new Motor[3];
		motors[0] = new Motor(MotorPort.A);
		motors[1] = new Motor(MotorPort.B);
		motors[2] = new Motor(MotorPort.C);
	}
	
	private void initSensors(String[] config) {
		sensors = new Sensor[4];
		initSensor(SensorPort.S1, 0, new Integer(config[1]));
		initSensor(SensorPort.S2, 1, new Integer(config[2]));
		initSensor(SensorPort.S3, 2, new Integer(config[3]));
		initSensor(SensorPort.S4, 3, new Integer(config[4]));
	}
	
	private void initSensor(SensorPort port, int portNumber, int type) {
		switch (type) {
			case NxtProtocol.NO_SENSOR:
				break;
			case NxtProtocol.ULTRASONIC_SENSOR:
				sensors[portNumber] = new NxtUltrasonicSensor(port);
				break;
			case NxtProtocol.LIGHT_SENSOR:
				sensors[portNumber] = new NxtLightSensor(port);
				break;
			case NxtProtocol.SOUND_SENSOR:
				sensors[portNumber] = new NxtSoundSensor(port);
				break;
			case NxtProtocol.TOUCH_SENSOR:
				sensors[portNumber] = new NxtTouchSensor(port);
				break;
			case NxtProtocol.RFID_SENSOR:
				sensors[portNumber] = new NxtRFIDSensor(port);
				break;
		}
	}
	
	private void initPilot(String[] config) {
		boolean motorReverse = new Integer(config[9]) == 1 ? true : false;
		pilot = new TachoPilot(new Float(config[7]), new Float(config[8]),
				motors[new Integer(config[5]) - 1], motors[new Integer(config[6]) - 1], motorReverse);
		pilot.setMoveSpeed(15);
		pilot.regulateSpeed(true);
	}
	
	// Commands
	
	private void exit() {
		System.exit(0);
	}
	
	private void sendSensorData() throws Exception {
		StringBuffer result = new StringBuffer();
		result.append(NxtProtocol.SENSOR_DATA);
		result.append(';');
		for(int i = 0; i < data.sensorValues.length; i++) {
			result.append(data.sensorValues[i]);
			result.append(';');
		}
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
