package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

import org.vu.aso.next.pc.NxtBridge;

public class ScanPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -4810536547608546819L;

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		try {
			robot.performScan(2, -85, 85);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
