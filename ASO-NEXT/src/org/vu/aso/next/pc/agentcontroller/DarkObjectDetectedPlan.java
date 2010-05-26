package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.NxtSettings;

public class DarkObjectDetectedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public DarkObjectDetectedPlan() {
	}

	@Override
	public void body() {
		if (NxtSettings.DEFAULT_PRINT_DEBUG_SETTING)
			printDebug("executed DarkObjectDetectedPlan()");

		// Carrying a black object now
		setBelief(Beliefs.CARRYING_WHITE_BLOCK, false);
		setBelief(Beliefs.CARRYING_BLACK_BLOCK, true);
	}
}