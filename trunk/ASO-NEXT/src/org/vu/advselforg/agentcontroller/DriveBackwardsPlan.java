package org.vu.advselforg.agentcontroller;

import java.io.IOException;

import org.vu.advselforg.oud.RobotController;
import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.Plan;

public class DriveBackwardsPlan extends Plan{

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		setBelief("drivingBackwards", true);
		try {
			robot.MoveBackward(60);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
	private void setBelief(String BeliefName, Object beliefValue) {
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
		getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
	}
}
