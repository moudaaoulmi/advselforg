package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.Plan;

public class ActionScanArea extends Plan{

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		
	}

}
