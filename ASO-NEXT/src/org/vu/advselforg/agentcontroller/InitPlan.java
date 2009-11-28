package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.robotcontroller.*;

import sun.awt.EmbeddedFrame;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.IFilter;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class InitPlan extends Plan implements Runnable{
	
	private static final long serialVersionUID = 2961883448970106007L;
	NxtController robot;
	@Override
	public void body() {
			
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		SensorType port1 = SensorType.TOUCH;
		SensorType port2 = SensorType.TOUCH;
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.NONE;
		try {
			robot = new NxtController("Patrick", leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);	
		} catch (Exception e) {
			System.out.println("doet ut niet");
		}

		new Thread(this).start();
		
		getBeliefbase().getBelief("robot").setFact(robot);
		System.out.println("Connected");
		
		getBeliefbase().getBelief("notInitialized").setFact(false);
		waitFor(IFilter.NEVER);
		
	}
	
	
	@Override
	public void run() {
		while(true){
			if(robot.getTouchSensorPressed(0)){
			
				IMessageEvent me = getExternalAccess().createMessageEvent("berichtSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier("Mindstorms",true));
				me.setContent("TouchSensorStatus " + "Pressed 1");
				getExternalAccess().sendMessage(me);
			}
			if(robot.getTouchSensorPressed(1)){
				IMessageEvent me = getExternalAccess().createMessageEvent("berichtSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier("Mindstorms",true));
				me.setContent("TouchSensorStatus " + "Pressed 2");
				getExternalAccess().sendMessage(me);
			}
		}
	}		
}

