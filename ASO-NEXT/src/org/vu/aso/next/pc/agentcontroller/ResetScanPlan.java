package org.vu.aso.next.pc.agentcontroller;

public class ResetScanPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 6054116585490038881L;

	public ResetScanPlan() {
	}

	@Override
	public void body() {
		printDebug("executed ResetScanPlan()");
		setBelief("areaScanned", false);
		getRobot().resetTravelDistance();
	}
}