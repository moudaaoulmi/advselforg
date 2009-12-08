package test;

import java.io.Console;
import java.io.IOException;
import java.util.StringTokenizer;

import org.vu.advselforg.robotcontroller.Motorport;
import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.SensorType;

public class KKP {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		
		NxtBridge bridge = new NxtBridge("ROSS", SensorType.ULTRASONIC, SensorType.ULTRASONIC, SensorType.LIGHT, 
				SensorType.NONE, Motorport.A, Motorport.C, 5.4f, 15.1f, false);
		double distance = 20;
		bridge.Drive(distance);
		bridge.close();
		
		
	}
	
	
	

}
