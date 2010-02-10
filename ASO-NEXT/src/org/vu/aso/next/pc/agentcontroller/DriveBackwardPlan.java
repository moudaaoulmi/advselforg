package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class DriveBackwardPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public DriveBackwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBelief("drivingBackward", true);
	}
}
