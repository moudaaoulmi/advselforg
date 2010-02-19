package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class AvoidObstaclePlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int angle;

	public AvoidObstaclePlan() {
		this.angle = 180;
	}

	@Override
	public void body() {
		printDebug("executed AvoidObstaclePlan()");

		setBelief("readyForCommand", false);

		try {
			getRobot().turnLeft(angle);
			setBelief("turning", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//readyForCommand true door WIM
	}
}
