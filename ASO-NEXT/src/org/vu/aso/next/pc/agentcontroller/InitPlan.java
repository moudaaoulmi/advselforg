package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

import jadex.runtime.Plan;

public class InitPlan extends Plan {

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
		ESensorType port1 = ESensorType.ULTRASONIC;
		ESensorType port2 = ESensorType.ULTRASONIC;
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		float wheelDiameter = 5.4f;
		float trackWidth = 15.1f;
		try {
			robot = new NxtBridge(agentName, port1, port2, port3, port4, leftMotorPort, rightMotorPort, motorReverse, wheelDiameter, trackWidth);

			getBeliefbase().getBelief("robot").setFact(robot);
			getBeliefbase().getBelief("robot").modified();
			System.out.println("Connected");

			getBeliefbase().getBelief("Initialized").setFact(true);
			getBeliefbase().getBelief("Initialized").modified();

		} catch (Exception e) {
			System.out.println("Whoops, does not compute. Cannot connect to robot.");
		}
	}
}
