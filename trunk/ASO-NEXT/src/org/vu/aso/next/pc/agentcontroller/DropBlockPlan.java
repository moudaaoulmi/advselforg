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
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		getRobot().driveBackward(distance);
		setBelief(Beliefs.DRIVING_BACKWARD, true);
		waitForBeliefChange(Beliefs.DRIVING_BACKWARD);
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		getRobot().turnLeft(angle);
		setBelief(Beliefs.TURNING, true);
		
		setBelief("carryingWhiteBlock", false);
		setBelief("carryingBlackBlock", false);
	}
}