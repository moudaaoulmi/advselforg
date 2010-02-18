package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class DropBlockPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;
	private int angle;

	public DropBlockPlan() {
		this.distance = 60;
		this.angle = 180;
	}

	@Override
	public void body() {
		printDebug("executed DropBlockPlan(" + distance + ", " + angle + ")");
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while ((Boolean) getBelief("drivingBackward")) {}
		printDebug("finished driving backward");
		
		try {
			getRobot().turnLeft(angle);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setBelief("readyForCommand", true);
	}
}