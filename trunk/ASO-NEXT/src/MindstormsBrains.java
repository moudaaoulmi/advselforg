import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.robotics.navigation.TachoPilot;

import java.io.*;
import java.util.StringTokenizer;

public class MindstormsBrains {

	private OutputStream outDat;
	private InputStream inDat;
	
	private Motor[] motors;
	private TachoPilot pilot;
	
	MindstormsBrains() {
		initMotors();
		initPilot();
		
		LCD.drawString("Waiting...", 0, 0);
		LCD.refresh();
		BTConnection conn = Bluetooth.waitForConnection();
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		outDat = conn.openOutputStream();
		inDat = conn.openInputStream();
	}
	
	private void run() throws Exception {
		int i = 0;
		while (true) {
			String[] message = getMessage();
			
			if (message[0].charAt(0) != '\u0000') {
				int command = new Integer(message[0]);
				switch (command) {
					case 0:
						System.exit(0);
						break;
					case 1:
						LCD.drawString("Forward!", 0, 0);
						float distance = new Float(message[1]);
						pilot.travel(distance, false);
						break;
					case 2:
						String ack = "2;";
						outDat.write(ack.getBytes(""));
						outDat.flush();
						LCD.drawString("ACK " + i + " sent", 0, 0);
						break;
					default:
						LCD.drawString("No command!", 0, 0);
						break;
				}
			}
			LCD.drawString("Step " + i, 0, 3);
			i++;
		}
	}

	private void initMotors() {
		motors = new Motor[3];
		motors[0] = new Motor(MotorPort.A);
		motors[1] = new Motor(MotorPort.B);
		motors[2] = new Motor(MotorPort.C);
	}
	
	private void initPilot() {
		pilot = new TachoPilot(5.4f, 15.1f, motors[0], motors[2], false);
		pilot.setMoveSpeed(15);
		pilot.regulateSpeed(true);
	}

	private String[] getMessage() throws Exception {
		String[] message = new String[10];
		byte[] buffer = new byte[30];

		inDat.read(buffer, 0, buffer.length);
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
