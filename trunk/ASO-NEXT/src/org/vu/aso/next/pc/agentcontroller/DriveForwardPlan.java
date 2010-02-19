package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class DriveForwardPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;
	private int distance;

	public DriveForwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		printDebug("executed DriveForwardPlan(" + distance + ")");
		
		try {
			getRobot().moveForward(distance);
			setBelief("drivingForward", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setBelief("readyForCommand", true);
	}
}
