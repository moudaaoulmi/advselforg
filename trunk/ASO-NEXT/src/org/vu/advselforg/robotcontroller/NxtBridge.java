package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.EMovingMode;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.common.NxtProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import lejos.nxt.remote.NXTProtocol;
import lejos.pc.comm.NXTConnector;

public class NxtBridge {

	NXTConnector conn;
	InputStream in;
	OutputStream out;
	SensorData sensorData = new SensorData();

	public NxtBridge(String nxtName, ESensorType port1, ESensorType port2, ESensorType port3,
			ESensorType port4, EMotorPort pilotPortLeft, EMotorPort pilotPortRight,
			Boolean MotorReverse, float wheelDiameter, float trackWidth)
			throws InterruptedException, IOException {

		conn = new NXTConnector();
		conn.connectTo("btspp://" + nxtName);

		in = conn.getInputStream();
		out = conn.getOutputStream();
		Thread.sleep(1000);

		int reverse = MotorReverse ? 1 : 0;

		String initMessage = buildMessage(wheelDiameter, trackWidth, NxtProtocol.INIT, port1
				.ordinal(), port2.ordinal(), port3.ordinal(), port4.ordinal(), pilotPortLeft
				.ordinal(), pilotPortRight.ordinal(), reverse);

		communicateToNxt(initMessage);
	}

	public void MoveForward(int distance) throws IOException {
		String message = buildMessage(NxtProtocol.FORWARD, distance);
		sensorData.setLastCommand(EMovingMode.FORWARD);
		communicateToNxt(message);
	}

	public SensorData RequestSensorData() throws IOException {
		String message = buildMessage(NxtProtocol.SENSOR_DATA);
		sensorData.processMessage(communicateToNxt(message));
		// TODO
		return sensorData;// processResponse(sensorData);
	}

	public void MoveBackward(int distance) throws IOException {
		String message = buildMessage(NxtProtocol.BACKWARD, distance);
		sensorData.setLastCommand(EMovingMode.BACKWARD);
		communicateToNxt(message);
	}

	public void TurnLeft(int angle) throws IOException {
		String message = buildMessage(NxtProtocol.TURN_LEFT, angle);
		sensorData.setLastCommand(EMovingMode.TURNING);
		communicateToNxt(message);
	}

	public void TurnRight(int angle) throws IOException {
		String message = buildMessage(NxtProtocol.TURN_RIGHT, angle);
		sensorData.setLastCommand(EMovingMode.TURNING);
		communicateToNxt(message);
	}

	public void ResetTrafeldistance() throws IOException {
		String message = buildMessage(NxtProtocol.RESET_TRAVEL_DISTANCE);
		communicateToNxt(message);
	}

	public void PerformScan(int port, int fromAngle, int toAngle, int speed) throws IOException {
		String message = buildMessage(NxtProtocol.PERFORM_SCAN, fromAngle, toAngle, speed);
		communicateToNxt(message);
	}

	public void Stop() throws IOException {
		String message = buildMessage(NxtProtocol.STOP);
		communicateToNxt(message);
	}

	public void Exit() throws IOException {
		String message = buildMessage(NxtProtocol.EXIT);
		communicateToNxt(message);
	}

	public void close() throws IOException {
		this.Stop();
		this.Exit();
		conn.close();
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

		while ((b = (byte) in.read()) != -1) {
			sb.append((char) b);
		}
		return sb.toString().split(";");
	}
}
