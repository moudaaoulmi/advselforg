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
	String agentName;
	
	public InitPlan(String agentName){
		this.agentName = agentName;

	}
	
	
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
			System.out.println("Whoops, does not compute. Cannot connect to robot.");
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
			
			//Send message when a status is changed from a press sensor "TouchSensorStatus {relativeID} {pressed|released}"
			//Send a message for every cm that is moved forward. Reset after a stop/turn "TraveledDistance {x}"
			//Send message for rotating a motor. Start at leftMax, go to rightMax, go to 0 position. Whenever a problem is 
			//    encountered, always go to 0 position. "MotorRotationDone {relativeMotorId}"
			//    Rotations of the driving motor are excluded from this message stream.
			//Send a message when there is a tachometer problem. "TachoMeterProblem"
			//Send a message when a turn is complete, but only when it is correctly done. "TurnComplete"
			//Send a message when a drive backwards is complete, but only when it is correctly done. "DriveBackwardComplete"
			//Send a message for every sonar read. "SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}" 
			//Send a message for light change from nothing to black or white and one for having nothing.
			//    "LightSensorStatus {relativeLightId} {nothing|black|white}"
			
			if(robot.getTouchSensorPressed(0)){
			
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TouchSensorStatus " + "Pressed 1");
				getExternalAccess().sendMessage(me);
			}
			if(robot.getTouchSensorPressed(1)){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TouchSensorStatus " + "Pressed 2");
				getExternalAccess().sendMessage(me);
			}
		}
	}		
}

