package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class TurnRightPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6357593709536921293L;
	private int angle;

	public TurnRightPlan(int angle) {
		this.angle = angle;
	}

	@Override
	public void body() {
		try {
			getRobot().turnRight(angle);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
