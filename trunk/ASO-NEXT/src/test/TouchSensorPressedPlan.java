package test;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class TouchSensorPressedPlan extends Plan{

	@Override
	public void body() {
		
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);
		// TODO Auto-generated method stub
		//RobotController robot = (RobotController)getBeliefbase().getBelief("robot");
		//robot.moveForward(0, true);
		
	}

}
