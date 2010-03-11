package org.vu.aso.next.pc.agentcontroller;

public class DarkObjectDetectedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public DarkObjectDetectedPlan() {
	}

	@Override
	public void body() {
		printDebug("executed Dark Object Detected plan(");
		setBelief("carryingWhiteBlock", false);
		setBelief("carryingBlackBlock", true);
	}
}