package org.vu.aso.next.pc.agentcontroller;

public class AvoidClusterPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public AvoidClusterPlan() {
		this.distance = 60;
	}

	@Override
	public void body() {
		printDebug("executed AvoidObstaclePlan(" + distance + ")");

		setBelief(Beliefs.READY_FOR_COMMAND, false);
		
		getRobot().driveBackward(distance);
		setBelief(Beliefs.DRIVING_BACKWARD, true);
	}
}