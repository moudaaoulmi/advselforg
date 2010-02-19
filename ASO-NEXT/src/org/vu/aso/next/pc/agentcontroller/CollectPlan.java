package org.vu.aso.next.pc.agentcontroller;

public class CollectPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int angle;
	
	public CollectPlan(int angle) {
		this.angle = angle;
	}

	@Override
	public void body() {
		printDebug("executed AvoidObstaclePlan()");
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		
		getRobot().turnLeft(angle);
		waitForBeliefChange(Beliefs.TURNING);
		
		getRobot().driveForward(10);
		waitForBeliefChange(Beliefs.DRIVING_FORWARD);
		
		setBelief(Beliefs.READY_FOR_COMMAND, true);
	}
}