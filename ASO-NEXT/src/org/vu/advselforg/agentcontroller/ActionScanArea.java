package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.Plan;

public class ActionScanArea extends Plan{

	@Override
	public void body() {
		RobotController robot = (RobotController) getBeliefbase().getBelief("robot").getFact();
		
	}

}
