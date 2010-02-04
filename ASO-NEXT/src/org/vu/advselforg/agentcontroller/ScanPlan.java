package org.vu.advselforg.agentcontroller;

import java.io.IOException;

import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.Plan;

public class ScanPlan extends Plan {

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
