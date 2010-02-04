package org.vu.aso.next.pc.test;

import java.io.IOException;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

public class LightSensorTest {

	public static void main(String[] args) throws InterruptedException, IOException {

		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, ESensorType.ULTRASONIC, ESensorType.LIGHT,
				ESensorType.TOUCH, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);

		SensorData data;
		for (int i = 0; i < 1000; i++) {
			data = bridge.requestSensorData();
			System.out.println(data.getLightSensorValue() + " " + data.getObjectType());
		}

		bridge.close();
	}
}