

import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.*;


public class MindstormsBrains {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{
		//Compile nxjc  MindstormsBrains.java
		//Upload nxj -r MindstormsBrains -N JOEY

        LCD.drawString("Waiting...", 0, 0);
        LCD.refresh();
        BTConnection conn = Bluetooth.waitForConnection();
        LCD.clear();
        LCD.drawString("Connected", 0, 0);
        DataOutputStream outDat = conn.openDataOutputStream();
        DataInputStream inDat = conn.openDataInputStream();
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
        	byte[] b = new byte[10];
        	inDat.read(b);
        	String s = new String(b);
        	LCD.drawString(s, 0, 0);

     Button.waitForPress();   
        
	}

}
