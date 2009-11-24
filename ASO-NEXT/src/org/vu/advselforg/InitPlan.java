package org.vu.advselforg;

import jadex.runtime.Plan;

public class InitPlan extends Plan{
	
	private static final long serialVersionUID = 2961883448970106007L;
	NxtController robot;
	@Override
	public void body() {
			
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		SensorType port1 = SensorType.TOUCH;
		SensorType port2 = SensorType.NONE;
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.NONE;
		try {
			robot = new NxtController("Patrick", leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);	
		} catch (Exception e) {
			System.out.println("doet ut niet");
		}

		getBeliefbase().getBelief("robot").setFact(robot);
		System.out.println("Connected");
		
		getBeliefbase().getBelief("notInitialized").setFact(false);
		
	}
	

}
