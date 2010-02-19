package org.vu.aso.next.pc.test;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

public class SonarTest {

	public static void main(String[] args) {

		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, true, ESensorType.ULTRASONIC, true,
				ESensorType.LIGHT, true, ESensorType.TOUCH, true, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);

		SensorData data;
		for (int i = 0; i < 1000; i++) {
			data = bridge.requestSensorData();
			System.out.println(data.getDistanceLowerSonar() + " " + data.getDistanceUpperSonar());
		}

		bridge.close();
	}
}