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
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		
		getRobot().driveBackward(distance);

		setBelief(Beliefs.READY_FOR_COMMAND, true);
	}
}
