package org.vu.aso.next.pc.agentcontroller;

public class DriveForwardPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;
	private int distance;

	public DriveForwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		printDebug("executed DriveForwardPlan(" + distance + ")");
		
		// Drive forward for [distance] cm
		setBelief(Beliefs.DRIVING_FORWARD, true);
		getRobot().driveForward(distance);

		// Set readyForCommand true, so new plans can start
		setBelief(Beliefs.READY_FOR_COMMAND, true);
	}
}
