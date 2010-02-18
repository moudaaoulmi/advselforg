package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class DropBlockPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;

	public DropBlockPlan() {
		this.distance = 60;
	}

	@Override
	public void body() {
		printDebug("executed DropBlockPlan(" + distance + ")");
		try {
			getRobot().moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBelief("drivingBackward", true);
		
		while ((Boolean) getBelief("drivingBackward")) {}
		printDebug("finished driving backward");
		
		try {
			getRobot().turnLeft(90);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}