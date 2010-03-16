package org.vu.aso.next.pc.agentcontroller;

import java.util.Random;

public class DropBlockPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -8758789822720288236L;
	private int distance;
	private int angle;
	private Random r = new Random();

	public DropBlockPlan(int distance, int from, int to) {
		this.distance = distance;
		this.angle = r.nextInt(to - from + 1) + from;
	}

	@Override
	public void body() {
		printDebug("executed DropBlockPlan(" + distance + ", " + angle + ")");
		
		// Drive backward for [distance] cm
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		setBelief(Beliefs.DRIVING_BACKWARD, true);
		getRobot().driveBackward(distance);
		waitForBeliefChange(Beliefs.DRIVING_BACKWARD);
		
		// Turn [angle] degrees left
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		setBelief(Beliefs.TURNING, true);
		getRobot().turnLeft(angle);
		waitForBeliefChange(Beliefs.TURNING);
		
		// No longer carrying a block
		setBelief(Beliefs.CARRYING_WHITE_BLOCK, false);
		setBelief(Beliefs.CARRYING_BLACK_BLOCK, false);
	}
}