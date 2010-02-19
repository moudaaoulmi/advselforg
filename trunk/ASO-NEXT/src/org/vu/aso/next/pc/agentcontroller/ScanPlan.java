package org.vu.aso.next.pc.agentcontroller;

public class ScanPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -4810536547608546819L;

	@Override
	public void body() {
		printDebug("executed ScanPlan()");
		getRobot().performScan(-85, 85);
		
		setBelief("areaScanned", true);
	}

}
