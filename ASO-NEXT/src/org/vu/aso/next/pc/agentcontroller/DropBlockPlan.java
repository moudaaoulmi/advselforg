package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class DropBlockPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;
	private int angle;

	public DropBlockPlan() {
		this.distance = 20;
		this.angle = 180;
	}

	@Override
	public void body() {
		printDebug("executed DropBlockPlan(" + distance + ", " + angle + ")");
		
		setBelief("readyForCommand", false);
		try {
			getRobot().moveBackward(distance);
			setBelief("drivingBackward", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		waitForBeliefChange("drivingBackward");
		
		setBelief("readyForCommand", false);
		try {
			getRobot().turnLeft(angle);
			setBelief("turning", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}