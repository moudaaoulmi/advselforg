package org.vu.aso.next.pc.agentcontroller;

public class ScanPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -4810536547608546819L;

	@Override
	public void body() {
		printDebug("executed ScanPlan()");

		setBelief(Beliefs.READY_FOR_COMMAND, false);
		getRobot().stop();
		getRobot().performScan(85, -85);
		setBelief(Beliefs.SCANNING, true);
		waitForBeliefChange(Beliefs.SCANNING);
		
		setBelief(Beliefs.AREA_SCANNED, true);
	}

}
