package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.NxtSettings;

public class StopPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6604377277788735724L;

	@Override
	public void body() {
		if (NxtSettings.DEFAULT_PRINT_DEBUG_SETTING)
			printDebug("executed StopPlan()");

		// Stop the robot
		getRobot().stop();
	}

}
