package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class TurnLeftPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6357593709536921293L;
	private int angle;

	public TurnLeftPlan(int angle) {
		this.angle = angle;
	}

	@Override
	public void body() {
		printDebug("executed TurnLeftPlan(" + angle + ")");
		try {
			getRobot().turnLeft(angle);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
