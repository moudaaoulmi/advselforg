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
		
		getRobot().driveForward(distance);
		
		setBelief(Beliefs.READY_FOR_COMMAND, true);
	}
}
