package org.vu.advselforg.agentcontroller;

import java.io.IOException;
import java.util.Random;

import org.vu.advselforg.robotcontroller.NxtBridge;

import jadex.runtime.Plan;

public class TurnPlan extends Plan {

	private static final long serialVersionUID = -6357593709536921293L;

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		Random rnd = new Random();
		try {
			if (rnd.nextBoolean()) {
				robot.turnLeft(90);
			} else {
				robot.turnRight(90);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
