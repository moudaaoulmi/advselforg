import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.nxt.remote.RemoteSensorPort;
import lejos.robotics.navigation.TachoPilot;

import java.io.*;
import java.util.StringTokenizer;

public class MindstormsBrains {
	
	private OutputStream out;
	private InputStream in;
	
	private Motor[] motors;
	private SensorPort[] sensors;
	private TachoPilot pilot;
	
	MindstormsBrains() {
		initMotors();
		
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		BTConnection conn = Bluetooth.waitForConnection();
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		out = conn.openOutputStream();
		in = conn.openInputStream();
	}
	
	private void run() throws Exception {
		int step = 0;
		while (step < 1000) {
			String[] message = getMessage();
			
			if (message[0].charAt(0) != '\u0000') {
				int command = new Integer(message[0]);
				switch (command) {
					case 0:
						System.exit(0);
						break;
					case 1:
						sendMessage("1;");
						init(message);
						break;
					case 2:
						break;
					default:
						LCD.drawString("No command!", 0, 0);
						break;
				}
			}
			LCD.drawString("Step " + step, 0, 3);
			step++;
		}
	}
	
	private void init(String[] config) {
		
	}

	private void initMotors() {
		motors = new Motor[4];
		motors[1] = new Motor(MotorPort.A);
		motors[2] = new Motor(MotorPort.B);
		motors[3] = new Motor(MotorPort.C);
	}
	
	private void initSensor() {
		sensors = new SensorPort[5];

	}
	
	private void initPilot() {
		pilot = new TachoPilot(5.4f, 15.1f, motors[0], motors[2], false);
		pilot.setMoveSpeed(15);
		pilot.regulateSpeed(true);
	}
	
	private void sendMessage(String message) throws IOException {
		out.write(message.getBytes(""));
		out.write(-1);
		out.flush();
	}

	private String[] getMessage() throws Exception {
		String[] message = new String[10];
		byte[] buffer = new byte[30];

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
