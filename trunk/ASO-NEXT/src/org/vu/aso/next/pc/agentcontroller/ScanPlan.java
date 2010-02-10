package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class ScanPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -4810536547608546819L;

	@Override
	public void body() {
		printDebug("executed ScanPlan()");
		try {
			getRobot().performScan(-85, 85);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
