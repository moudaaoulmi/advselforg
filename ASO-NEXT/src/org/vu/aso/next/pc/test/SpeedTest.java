package test;

import java.io.Console;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;

import java.text.SimpleDateFormat;

import javax.management.timer.TimerNotification;


import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.SensorData;

public class SpeedTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, ESensorType.ULTRASONIC, ESensorType.LIGHT, 
				ESensorType.TOUCH, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);
		 
		long oldTime;
		long newTime;
		Thread.sleep(500);
		bridge.MoveForward(500);
		for(int i = 0; i <100; i++){
			oldTime = System.currentTimeMillis();
			bridge.RequestSensorData();
			newTime = System.currentTimeMillis();
			System.out.println(newTime - oldTime);
			oldTime = newTime;

		}
		bridge.close();
		
	}

}
