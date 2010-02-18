package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

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
		
		try {
			getRobot().turnLeft(angle);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while ((Boolean) getBelief("turning")) {}
		
		setBelief("topSonarDistance", 255);
		
		try {
			getRobot().moveForward(10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while ((Boolean) getBelief("drivingForward")) {}
		
		setBelief("readyForCommand", true);
	}
}