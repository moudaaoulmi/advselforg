package org.vu.aso.next.pc;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.EMovingMode;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.common.NxtProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import lejos.pc.comm.NXTConnector;

public class NxtBridge {

	private NXTConnector connection;
	private InputStream in;
	private OutputStream out;
	private SensorData sensorData = new SensorData();
	private FileOutputStream logfile;
	private PrintStream logstream;
	private SimpleDateFormat formatter;

	public NxtBridge(String nxtName, ESensorType port1, boolean monitorPort1, ESensorType port2, boolean monitorPort2,
			ESensorType port3, boolean monitorPort3, ESensorType port4, boolean monitorPort4, EMotorPort pilotPortLeft,
			EMotorPort pilotPortRight, Boolean MotorReverse, float wheelDiameter, float trackWidth)
			throws InterruptedException, IOException {

		logfile = new FileOutputStream("C:\\log.txt");
		logstream = new PrintStream(logfile);
		
		formatter = new SimpleDateFormat("HH:mm:ss.SSS");

		connection = new NXTConnector();
		connection.connectTo("btspp://" + nxtName);

		in = connection.getInputStream();
		out = connection.getOutputStream();
		Thread.sleep(1000);

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

	public SensorData requestSensorData() throws IOException {
		String message = buildMessage(NxtProtocol.SENSOR_DATA);
		sensorData.processMessage(communicateToNxt(message));
		return sensorData;
	}

	public void moveForward(int distance) throws IOException {
		String message = buildMessage(NxtProtocol.FORWARD, distance);
		sensorData.setLastCommand(EMovingMode.FORWARD);
		sensorData.setMoving(true);
		communicateToNxt(message);
	}

	public void moveBackward(int distance) throws IOException {
		String message = buildMessage(NxtProtocol.BACKWARD, distance);
		sensorData.setLastCommand(EMovingMode.BACKWARD);
		sensorData.setMoving(true);
		communicateToNxt(message);
	}

	public void turnLeft(int angle) throws IOException {
		String message = buildMessage(NxtProtocol.TURN_LEFT, angle);
		sensorData.setLastCommand(EMovingMode.TURNING);
		sensorData.setMoving(true);
		communicateToNxt(message);
	}

	public void turnRight(int angle) throws IOException {
		String message = buildMessage(NxtProtocol.TURN_RIGHT, angle);
		sensorData.setLastCommand(EMovingMode.TURNING);
		sensorData.setMoving(true);
		communicateToNxt(message);
	}

	public void resetTravelDistance() throws IOException {
		String message = buildMessage(NxtProtocol.RESET_TRAVEL_DISTANCE);
		communicateToNxt(message);
	}

	public void calibrateTurret() throws IOException {
		String message = buildMessage(NxtProtocol.CALIBRATE_TURRET);
		communicateToNxt(message);
	}

	public void performScan(int fromAngle, int toAngle) throws IOException {
		String message = buildMessage(NxtProtocol.PERFORM_SCAN, fromAngle, toAngle);
		communicateToNxt(message);
	}

	public void stop() throws IOException {
		String message = buildMessage(NxtProtocol.STOP);
		communicateToNxt(message);
	}

	public void exit() throws IOException {
		String message = buildMessage(NxtProtocol.EXIT);
		communicateToNxt(message);
	}

	public void close() throws IOException {
		this.stop();
		this.exit();
		connection.close();
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

	private synchronized String[] communicateToNxt(String message) throws IOException {
		logstream.println(formatter.format(new Date()) + " " + message);
		writeMessage(message);
		return getMessage();
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
		return sb.toString().split(";");
	}
}
