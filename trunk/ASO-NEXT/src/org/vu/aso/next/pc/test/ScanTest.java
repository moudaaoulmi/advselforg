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

		// Perform the scan
		bridge.performScan(-85, 85);
		do {
			data = bridge.requestSensorData();
		} while (data.isScanning());
		
		// Print the results
		System.out.println("Angle: " + data.getClosestblockAngle());
		System.out.println("Distance: " + data.getClosestblockDistance());
		
		// Turn towards the object
		/*bridge.turnRight(data.getClosestblockAngle());
		do {
			data = bridge.requestSensorData();
		} while (data.isTurning());
		
		// Drive towards the object
		bridge.driveForward(data.getClosestblockDistance());
		do {
			data = bridge.requestSensorData();
		} while (data.isDrivingForward());
*/
		bridge.close();
	}
}