package org.vu.aso.next.pc;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.EMovingMode;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.common.NxtProtocol;
import org.vu.aso.next.common.NxtSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import lejos.pc.comm.NXTConnector;

public class NxtBridge {

	private NXTConnector connection;
	private InputStream in;
	private OutputStream out;
	private SensorData sensorData = new SensorData();
	private PrintStream logstream;
	private SimpleDateFormat formatter;

	public NxtBridge(String nxtName, ESensorType port1, boolean monitorPort1, ESensorType port2, boolean monitorPort2,
			ESensorType port3, boolean monitorPort3, ESensorType port4, boolean monitorPort4, EMotorPort pilotPortLeft,
			EMotorPort pilotPortRight, Boolean MotorReverse, float wheelDiameter, float trackWidth) {

		try {
			logstream = new PrintStream(NxtSettings.LOG_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		formatter = new SimpleDateFormat("HH:mm:ss.SSS");

		connection = new NXTConnector();
		connection.connectTo("btspp://" + nxtName);

		in = connection.getInputStream();
		out = connection.getOutputStream();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int reverse = MotorReverse ? NxtProtocol.TRUE : NxtProtocol.FALSE;
		int monitorPort1_int = monitorPort1 ? NxtProtocol.TRUE : NxtProtocol.FALSE;
		int monitorPort2_int = monitorPort2 ? NxtProtocol.TRUE : NxtProtocol.FALSE;
		int monitorPort3_int = monitorPort3 ? NxtProtocol.TRUE : NxtProtocol.FALSE;
		int monitorPort4_int = monitorPort4 ? NxtProtocol.TRUE : NxtProtocol.FALSE;

		String initMessage = buildMessage(wheelDiameter, trackWidth, NxtProtocol.INIT, port1.ordinal(),
				monitorPort1_int, port2.ordinal(), monitorPort2_int, port3.ordinal(), monitorPort3_int,
				port4.ordinal(), monitorPort4_int, pilotPortLeft.ordinal(), pilotPortRight.ordinal(), reverse);

		communicateToNxt(initMessage);
	}

	public SensorData requestSensorData() {
		String message = buildMessage(NxtProtocol.SENSOR_DATA);
		sensorData.processMessage(communicateToNxt(message));
		return sensorData;
	}

	public void driveForward(int distance) {
		sensorData.setLastCommand(EMovingMode.FORWARD);
		sensorData.setMoving(true);
		String message = buildMessage(NxtProtocol.FORWARD, distance);
		communicateToNxt(message);
	}

	public void driveBackward(int distance) {
		sensorData.setLastCommand(EMovingMode.BACKWARD);
		sensorData.setMoving(true);
		String message = buildMessage(NxtProtocol.BACKWARD, distance);
		communicateToNxt(message);
	}

	public void turnLeft(int angle) {
		sensorData.setLastCommand(EMovingMode.TURNING);
		sensorData.setMoving(true);
		String message;
		if (angle >= 0) {
			message = buildMessage(NxtProtocol.TURN_LEFT, angle);
		} else {
			message = buildMessage(NxtProtocol.TURN_RIGHT, -1 * angle);
		}
		communicateToNxt(message);
	}

	public void turnRight(int angle) {
		sensorData.setLastCommand(EMovingMode.TURNING);
		sensorData.setMoving(true);
		String message = buildMessage(NxtProtocol.TURN_RIGHT, angle);
		if (angle >= 0) {
			message = buildMessage(NxtProtocol.TURN_RIGHT, angle);
		} else {
			message = buildMessage(NxtProtocol.TURN_LEFT, -1 * angle);
		}
		communicateToNxt(message);
	}

	public void resetTravelDistance() {
		String message = buildMessage(NxtProtocol.RESET_TRAVEL_DISTANCE);
		communicateToNxt(message);
	}

	public void stop() {
		String message = buildMessage(NxtProtocol.STOP);
		communicateToNxt(message);
	}

	public void exit() {
		String message = buildMessage(NxtProtocol.EXIT);
		communicateToNxt(message);
	}

	public void close() {
		this.stop();
		this.exit();
		try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SensorData getSensorData() {
		return this.sensorData;
	}

	// Build a normal message
	private String buildMessage(int... params) {
		StringBuilder sb = new StringBuilder();
		for (int cnt = 0; cnt < params.length; cnt++) {
			sb.append(params[cnt]);
			sb.append(";");
		}
		return sb.toString();
	}

	// Build the initMessage
	private String buildMessage(Float diameter, Float width, int... params) {
		StringBuilder sb = new StringBuilder(buildMessage(params));
		sb.append(diameter);
		sb.append(";");
		sb.append(width);
		sb.append(";");
		return sb.toString();
	}

	private synchronized String[] communicateToNxt(String message) {
		logstream.println(formatter.format(new Date()) + " -> " + message);
		try {
			writeMessage(message);
			return getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void writeMessage(String message) throws IOException {
		out.write(message.getBytes());
		out.flush();
	}

	private String[] getMessage() throws IOException {
		StringBuffer sb = new StringBuffer();
		byte b;

		while ((b = (byte) in.read()) != NxtProtocol.TERMINATION_BYTE) {
			sb.append((char) b);
		}
		logstream.println(formatter.format(new Date()) + " <- " + sb);
		return sb.toString().split(";");
	}
}
