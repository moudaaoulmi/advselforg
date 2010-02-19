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

		getRobot().driveBackward(distance);
		setBelief(Beliefs.DRIVING_BACKWARD, true);

		getRobot().driveBackward(distance);
		
	}
}