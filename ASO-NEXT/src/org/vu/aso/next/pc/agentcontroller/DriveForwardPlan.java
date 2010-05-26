package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.NxtSettings;

public class DriveForwardPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;
	private int distance;

	public DriveForwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		if (NxtSettings.DEFAULT_PRINT_DEBUG_SETTING)
			printDebug("executed DriveForwardPlan(" + distance + ")");

		// Drive forward for [distance] cm
		getRobot().driveForward(distance);
		setBelief(Beliefs.DRIVING_FORWARD, true);
	}
}
