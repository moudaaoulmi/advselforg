package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

import org.vu.aso.next.pc.NxtBridge;

import jadex.runtime.Plan;

public class DriveForwardPlan extends Plan {

	private static final long serialVersionUID = 6054116585490038881L;

	private int distance;

	public DriveForwardPlan(int distance) {
		this.distance = distance;
	}

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		try {
			robot.moveForward(distance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
