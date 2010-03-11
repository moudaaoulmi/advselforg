package org.vu.aso.next.pc.agentcontroller;

import java.util.Random;

public class RandomTurnPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int angle;
	private Random r = new Random();

	public RandomTurnPlan(int from, int to) {
		this.angle = r.nextInt(to - from + 1) + from;
	}

	@Override
	public void body() {
		printDebug("executed RandomTurnPlan()");

		setBelief(Beliefs.READY_FOR_COMMAND, false);
		getRobot().turnLeft(angle);
		setBelief(Beliefs.TURNING, true);
	}
}
