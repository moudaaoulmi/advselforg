package org.vu.advselforg.robotcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import lejos.pc.comm.NXTConnector;

public class NxtBridge {

	NXTConnector conn;
	InputStream in;
	OutputStream out;
	
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
	
	public void Drive(int distance) throws IOException{
		StringBuffer driveMessage = new StringBuffer();
		driveMessage.append(4);
		driveMessage.append(";");
		driveMessage.append(distance);
		driveMessage.append(";");
		writeMessage(driveMessage.toString());
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
		StringBuffer sb = new StringBuffer();
		byte b;
		
		while((b = (byte) in.read()) != -1){
			sb.append((char) b);
		}
		System.out.println(sb.toString());
		sb.setLength(0);

		String incMessage = sb.toString();
        
		/*StringTokenizer st = new StringTokenizer(incMessage);
		if(sb.charAt(0) == 3){
			processRespnse(incMessage);
		}*/
		return true;
	}


	private void processRespnse(String incMessage) {
		// TODO Auto-generated method stub
		
	}
	

}
