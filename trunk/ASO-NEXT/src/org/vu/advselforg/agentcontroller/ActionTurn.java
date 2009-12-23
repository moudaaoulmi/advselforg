package org.vu.advselforg.agentcontroller;

import java.io.IOException;
import java.util.Random;

import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.Plan;

public class ActionTurn extends Plan{

	@Override
	public void body() {
		NxtBridge robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		Random rnd = new Random();
		try {
			if(rnd.nextBoolean())
			{
				robot.TurnLeft(90);
			}else{
				robot.TurnRight(90);
			}
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
}
