package test;

import java.io.IOException;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.SensorData;

public class TachometerTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, ESensorType.ULTRASONIC, ESensorType.LIGHT, 
				ESensorType.TOUCH, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);
		
		SensorData data;
		
		bridge.performScan(1, 90, -90);
		do {
			data = bridge.requestSensorData();
		} while (data.isScanning());
		System.out.println("Angle: " + data.getClosestblockAngle());
		System.out.println("Distance: " + data.getClosestblockDistance());
		
		bridge.close();
	}
}