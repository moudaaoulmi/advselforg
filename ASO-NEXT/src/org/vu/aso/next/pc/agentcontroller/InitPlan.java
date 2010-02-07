package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

public class InitPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 2961883448970106007L;

	String agentName;

	public InitPlan(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public void body() {
		NxtBridge robot;

		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		ESensorType port1 = ESensorType.ULTRASONIC; // Lower sensor
		ESensorType port2 = ESensorType.ULTRASONIC; // Upper sensor
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		float wheelDiameter = 5.4f;
		float trackWidth = 15.1f;
		try {
			robot = new NxtBridge(agentName, port1, false, port2, true, port3, true, port4, true, leftMotorPort,
					rightMotorPort, motorReverse, wheelDiameter, trackWidth);

			setBelief("robot", robot);
			System.out.println("Connected");

			setBelief("Initialized", true);

		} catch (Exception e) {
			System.out.println("Whoops, does not compute. Cannot connect to robot.");
		}
	}
}
