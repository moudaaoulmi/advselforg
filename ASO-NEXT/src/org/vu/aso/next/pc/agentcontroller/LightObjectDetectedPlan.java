package org.vu.aso.next.pc.agentcontroller;

public class LightObjectDetectedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public LightObjectDetectedPlan() {
	}

	@Override
	public void body() {
		printDebug("executed LightObjectDetectedPlan()");
		
		// Carrying a white object now
		setBelief(Beliefs.CARRYING_WHITE_BLOCK, true);
		setBelief(Beliefs.CARRYING_BLACK_BLOCK, false);
	}
}