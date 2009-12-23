package test;

import java.io.Console;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;

import java.text.SimpleDateFormat;

import javax.management.timer.TimerNotification;


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
		
		NxtBridge bridge = new NxtBridge("JOEY", SensorType.ULTRASONIC, SensorType.ULTRASONIC, SensorType.LIGHT, 
				SensorType.TOUCH, Motorport.A, Motorport.C, false, 5.4f, 15.1f);
		
		Date dat = new Date(); 
		 
		long oldTime;
		long newTime;
		Thread.sleep(500);
		bridge.MoveForward(500);
		for(int i = 0; i <100; i++){
			oldTime = System.currentTimeMillis();
			String[] incMessage = bridge.RequestSensorData();
			if(incMessage[4].equals("1")){
				bridge.Stop();
			}
			newTime = System.currentTimeMillis();
			System.out.println(newTime - oldTime);
			oldTime = newTime;

		}
		bridge.close();
		
		
	}
	
	
	

}
