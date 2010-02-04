package test;

import java.io.IOException;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.SensorData;

public class LightSensorTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, ESensorType.ULTRASONIC, ESensorType.LIGHT, 
				ESensorType.TOUCH, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);
		
		SensorData data;
		for (int i = 0; i < 1000; i++) {
			data = bridge.RequestSensorData();
			System.out.println(data.getLightSensorValue() + " " + data.getObjectType());
		}
		
		bridge.close();
	}
}