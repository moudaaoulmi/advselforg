package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

import org.vu.aso.next.pc.NxtBridge;

import jadex.runtime.Plan;

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
		setBelief("drivingBackward", true);
	}

	private void setBelief(String BeliefName, Object beliefValue) {
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
		getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
	}
}
