

import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.*;


public class MindstormsBrains {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{
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
        BufferedReader d = new BufferedReader(new InputStreamReader(inDat));

        
        //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.openInputStream()));
        LCD.clear();
        LCD.drawString(d.readLine(), 0, 0);
        
        outDat.writeChars("Message from Mindstorms");
        
        //OpticalDistanceSensor USS = new OpticalDistanceSensor(SensorPort.S2);
        Button.waitForPress();
        
	}

}
