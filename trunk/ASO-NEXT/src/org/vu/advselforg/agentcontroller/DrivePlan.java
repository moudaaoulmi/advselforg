package org.vu.advselforg.agentcontroller;

import java.io.IOException;

import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class DrivePlan extends Plan{

	@Override
	public void body() {

		NxtBridge robot = (NxtBridge)getBeliefbase().getBelief("robot");
		try {
			robot.MoveForward(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
