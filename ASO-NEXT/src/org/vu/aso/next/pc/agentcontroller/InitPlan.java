package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

public class InitPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 2961883448970106007L;

	String robotName;

	public InitPlan(String robotName) {
		this.robotName = robotName;
		setBelief(Beliefs.ROBOT_NAME, robotName);
	}

	@Override
	public void body() {
		printDebug("executed initPlan");
		NxtBridge robot;

		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		ESensorType port1 = ESensorType.NONE;
		ESensorType port2 = ESensorType.ULTRASONIC;
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		float wheelDiameter = 5.4f;
		float trackWidth = 15.1f;

		robot = new NxtBridge(robotName, port1, false, port2, true, port3, true, port4, true, leftMotorPort,
				rightMotorPort, motorReverse, wheelDiameter, trackWidth);

		setBelief(Beliefs.ROBOT, robot);
		setBelief(Beliefs.INITIALIZED, true);

		printDebug("is connected");
	}
}
