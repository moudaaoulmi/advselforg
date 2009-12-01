package org.vu.advselforg.agentcontroller;

import java.util.Random;

import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.Plan;

public class ActionTurn extends Plan{

	@Override
	public void body() {
		RobotController robot = (RobotController) getBeliefbase().getBelief("robot").getFact();
		Random rnd = new Random();
		if(rnd.nextBoolean())
		{
			robot.turnLeft(90, true);
		}else{
			robot.turnRight(90, true);
		}
	}

}
