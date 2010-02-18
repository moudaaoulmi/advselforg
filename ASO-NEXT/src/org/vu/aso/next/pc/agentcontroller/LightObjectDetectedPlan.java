package org.vu.aso.next.pc.agentcontroller;

public class LightObjectDetectedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public LightObjectDetectedPlan() {
	}

	@Override
	public void body() {
		printDebug("executed LightObjectDetectedPlan()");
		setBelief("searching", true);
		setBelief("collecting", false);
		setBelief("depositAtWall", false);
		setBelief("areaScanned", false);
	}
}