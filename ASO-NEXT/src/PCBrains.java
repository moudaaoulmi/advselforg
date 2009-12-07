
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        //BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
		byte[] byteArray = new byte[] {87, 79, 87, 46, 46, 46};
		//outDat.write
        //outDat.write(byteArray, 0, 6);
        
        //System.out.println(reader.readLine());
		
	}

}
