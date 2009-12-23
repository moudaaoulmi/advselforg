package org.vu.advselforg.agentcontroller;

import java.io.IOException;

import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.Plan;

public class ActionDriveForward extends Plan {

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		try {
			robot.MoveForward(1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
