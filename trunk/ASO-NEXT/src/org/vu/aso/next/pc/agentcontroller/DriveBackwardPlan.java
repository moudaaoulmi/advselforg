package org.vu.aso.next.pc.agentcontroller;

public class DriveBackwardPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public DriveBackwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		printDebug("executed DriveBackwardPlan(" + distance + ")");
		
		// Make sure no other plans are executed
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		
		// Drive backward for [distance] cm
		setBelief(Beliefs.DRIVING_BACKWARD, true);
		getRobot().driveBackward(distance);
		waitForBeliefChange(Beliefs.DRIVING_BACKWARD);
		
		// Ready for new command
		setBelief(Beliefs.READY_FOR_COMMAND, true);
	}
}
