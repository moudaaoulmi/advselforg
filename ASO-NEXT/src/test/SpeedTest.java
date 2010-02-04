package test;

import java.io.IOException;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.NxtBridge;

public class SpeedTest {

	public static void main(String[] args) throws InterruptedException, IOException {

		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, ESensorType.ULTRASONIC, ESensorType.LIGHT,
				ESensorType.TOUCH, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);

		long oldTime;
		long newTime;
		Thread.sleep(500);
		bridge.moveForward(500);
		for (int i = 0; i < 100; i++) {
			oldTime = System.currentTimeMillis();
			bridge.requestSensorData();
			newTime = System.currentTimeMillis();
			System.out.println(newTime - oldTime);
			oldTime = newTime;

		}
		bridge.close();

	}

}
