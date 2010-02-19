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
		
		setBelief("readyForCommand", false);
		
		getRobot().turnLeft(angle);
		
		while ((Boolean) getBelief("turning")) {}
		
		setBelief("topSonarDistance", 255);
		
		getRobot().driveForward(10);
		
		while ((Boolean) getBelief("drivingForward")) {}
		
		setBelief("readyForCommand", true);
	}
}