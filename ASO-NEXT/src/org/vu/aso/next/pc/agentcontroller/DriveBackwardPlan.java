package org.vu.aso.next.pc.agentcontroller;

import jadex.runtime.Plan;

import java.io.IOException;

import org.vu.aso.next.pc.NxtBridge;

public class DriveBackwardPlan extends Plan {

	private static final long serialVersionUID = -8758789822720288236L;
	int distance;

	public DriveBackwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		try {
			robot.moveBackward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getBeliefbase().getBelief("drivingBackward").setFact(true);
		getBeliefbase().getBelief("drivingBackward").modified();
		
		//setBelief("drivingBackward", true);
	}
	
	

}
