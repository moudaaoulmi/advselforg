package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;

public class InitPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 2961883448970106007L;

	String robotName;

	public InitPlan(String robotName) {
		this.robotName = robotName;
		setBelief(Beliefs.ROBOT_NAME, robotName);
	}

	@Override
	public void body() {
		NxtAgent robot;

		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		ESensorType port1 = ESensorType.ULTRASONIC; // Lower sensor
		ESensorType port2 = ESensorType.ULTRASONIC; // Upper sensor
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		float wheelDiameter = 5.4f;
		float trackWidth = 15.1f;

		robot = new NxtAgent(robotName, port1, false, port2, true, port3, true, port4, true, leftMotorPort,
				rightMotorPort, motorReverse, wheelDiameter, trackWidth, getExternalAccess());

		setBelief(Beliefs.ROBOT, robot);
		setBelief(Beliefs.INITIALIZED, true);

		printDebug("is connected");
	}
}
