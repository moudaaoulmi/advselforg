package org.vu.advselforg.agentcontroller;

import java.io.IOException;

import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.Plan;

public class DriveBackwardsPlan extends Plan{
	
	int distance;
	public DriveBackwardsPlan(int distance){
		this.distance = distance;
		
	}

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		try {
			robot.MoveBackward(distance);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setBelief("drivingBackwards", true);
		
	} 
	private void setBelief(String BeliefName, Object beliefValue) {
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
		getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
	}
}
