package test;
import lejos.nxt.*;
import lejos.nxt.comm.*;

import java.io.*;


public class MindstormsBrains {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Compile nxjc  MindstormsBrains.java
		//Upload nxj -r MindstormsBrains -N JOEY
		
		// TODO Auto-generated method stub
        LCD.drawString("Waiting...", 0, 0);
        LCD.refresh();
        BTConnection conn = Bluetooth.waitForConnection();
        LCD.clear();
        LCD.drawString("Connected", 0, 0);
        DataOutputStream outDat = conn.openDataOutputStream();
        DataInputStream inDat = conn.openDataInputStream();
        
        
        
        //OpticalDistanceSensor USS = new OpticalDistanceSensor(SensorPort.S2);
        Button.waitForPress();
        
	}

}
