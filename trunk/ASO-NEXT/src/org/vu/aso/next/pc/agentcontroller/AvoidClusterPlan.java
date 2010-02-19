package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class AvoidClusterPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public AvoidClusterPlan() {
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
		
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}