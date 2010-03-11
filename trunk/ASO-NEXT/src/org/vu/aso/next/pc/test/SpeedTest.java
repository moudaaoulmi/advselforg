package org.vu.aso.next.pc.test;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

public class SpeedTest {

	public static void main(String[] args) {

		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.NONE, false, ESensorType.ULTRASONIC, true,
				ESensorType.LIGHT, true, ESensorType.TOUCH, true, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);

		long oldTime;
		long newTime;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bridge.driveForward(500);
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
