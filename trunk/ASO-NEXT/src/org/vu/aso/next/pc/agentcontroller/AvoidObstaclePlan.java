package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class AvoidObstaclePlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public AvoidObstaclePlan() {
		this.distance = 60;
	}

	@Override
	public void body() {
		printDebug("executed AvoidObstaclePlan(" + distance + ")");
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBelief("drivingBackward", true);
		
		while (getRobot().getSensorData().isMoving());
		
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
