package org.vu.advselforg.robotcontroller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import lejos.pc.comm.NXTConnector;

public class NxtBridge {
	NXTConnector conn;
	InputStream in;
	OutputStream out;
	
	DataInputStream din;
	
	InputStreamReader reader;
	BufferedReader buff;
	
	public NxtBridge(String nxtName, SensorType port1, SensorType port2, SensorType port3, SensorType port4, 
			Motorport pilotPortLeft, Motorport pilotPortRight, float wheelDiameter, float trackWidth, Boolean MotorReverse) throws InterruptedException, IOException
	{
		
		conn = new NXTConnector();
		conn.connectTo("btspp://"+nxtName);
		
		in = conn.getInputStream();
		out = conn.getOutputStream();
		Thread.sleep(500);
		
		StringBuffer initMessage = new StringBuffer();
		initMessage.append(1);
		initMessage.append(";");
		initMessage.append(port1.ordinal());
		initMessage.append(";");
		initMessage.append(port2.ordinal());
		initMessage.append(";");
		initMessage.append(port3.ordinal());
		initMessage.append(";");
		initMessage.append(port4.ordinal());
		initMessage.append(";");
		initMessage.append(pilotPortLeft.ordinal());
		initMessage.append(";");
		initMessage.append(pilotPortRight.ordinal());
		initMessage.append(";");
		initMessage.append(wheelDiameter);
		initMessage.append(";");
		initMessage.append(trackWidth);
		initMessage.append(";");
		if(MotorReverse){
			initMessage.append(1);
		}else{
			initMessage.append(0);
		}
		initMessage.append(";");
		
		writeMessage(initMessage.toString());
		
	}
	
	public void Drive(Double distance){
		StringBuilder driveMessage = new StringBuilder();
		driveMessage.append(4);
		driveMessage.append(";");
		driveMessage.append(distance);
		driveMessage.append(";");
		
	}
	
	private void writeMessage(String message) throws IOException{
		out.write(message.getBytes());
		out.flush();
		getMessage();
		
		
	}
	
	public void close() throws IOException{
		conn.close();
	}
	
	private boolean getMessage() throws IOException{
		StringBuffer chars = new StringBuffer();
		
		byte x;
		while((x = (byte) in.read()) != -1){
			chars.append(x);
		}
		String incMessage = chars.toString();
        
		StringTokenizer st = new StringTokenizer(incMessage);
		if(chars.charAt(0) == 3){
			processRespnse(incMessage);
		}		
		return true;
	}


	private void processRespnse(String incMessage) {
		// TODO Auto-generated method stub
		
	}
	

}
