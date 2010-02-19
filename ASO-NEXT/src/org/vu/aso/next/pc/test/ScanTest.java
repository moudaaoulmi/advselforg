package org.vu.aso.next.pc.test;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

public class ScanTest {

	public static void main(String[] args) {

		NxtBridge bridge = new NxtBridge("JOEY", ESensorType.ULTRASONIC, true, ESensorType.ULTRASONIC, true,
				ESensorType.LIGHT, true, ESensorType.TOUCH, true, EMotorPort.A, EMotorPort.C, false, 5.4f, 15.1f);

		SensorData data;

		bridge.performScan(85, -85);
		do {
			data = bridge.requestSensorData();
		} while (data.isScanning());
		System.out.println("Angle: " + data.getClosestblockAngle());
		System.out.println("Distance: " + data.getClosestblockDistance());

		bridge.close();
	}
}