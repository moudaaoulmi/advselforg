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
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		try {
			getRobot().driveBackward(distance);
			setBelief(Beliefs.DRIVING_BACKWARD, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		waitForBeliefChange(Beliefs.DRIVING_BACKWARD);
		
		setBelief(Beliefs.READY_FOR_COMMAND, false);
		try {
			getRobot().turnLeft(angle);
			setBelief(Beliefs.TURNING, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}