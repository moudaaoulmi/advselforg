package org.vu.aso.next.pc.agentcontroller;

public class DarkObjectDetectedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public DarkObjectDetectedPlan() {
	}

	@Override
	public void body() {
		printDebug("executed DarkObjectDetectedPlan()");

		// Carrying a black object now
		setBelief(Beliefs.CARRYING_WHITE_BLOCK, false);
		setBelief(Beliefs.CARRYING_BLACK_BLOCK, true);
	}
}