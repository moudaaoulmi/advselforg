package org.vu.aso.next.pc.agentcontroller;

public class AvoidObstaclePlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int angle;

	public AvoidObstaclePlan() {
		this.angle = 180;
	}

	@Override
	public void body() {
		printDebug("executed AvoidObstaclePlan()");

		setBelief(Beliefs.READY_FOR_COMMAND, false);
		getRobot().turnLeft(angle);
		setBelief(Beliefs.TURNING, true);
	}
}
