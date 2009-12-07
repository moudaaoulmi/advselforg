package test;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.*;
public class PCBrains {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		NXTConnector conn = new NXTConnector();

		conn.connectTo("btspp://JOEY");
		
		DataOutputStream outDat = conn.getDataOut();
        DataInputStream inDat = conn.getDataIn();
        
        //BufferedReader reader = new BufferedReader(inDat.);
        
        outDat.writeChars("Message from laptop");
        
        
		
		
		
	}

}
