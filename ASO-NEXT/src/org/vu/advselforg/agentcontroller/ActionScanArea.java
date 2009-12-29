package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.oud.RobotController;
import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.Plan;

public class ActionScanArea extends Plan{

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		
	}

}
