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
	
	boolean touchPressed = false;
	int oldTraveledDistance = 0;
	
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
			
			//Send a message when the touch sensor is pressed and it was previously not pressed.
			if(robot.getTouchSensorPressed(0) && !touchPressed){
			
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TouchSensorStatus " + "0 pressed");
				getExternalAccess().sendMessage(me);
				touchPressed = true;
			}
			//Send a message when the touch sensor is released and it was previously pressed.
			if(!robot.getTouchSensorPressed(0) && touchPressed){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TouchSensorStatus " + "0 released");
				getExternalAccess().sendMessage(me);
				touchPressed = false;
			}
			
			//Send a message when the robot moved 1 cm forward or when it is resetted to 0.
			if(true){
				int traveledDistance = 0;
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TraveledDistance " + traveledDistance);
				getExternalAccess().sendMessage(me);
				oldTraveledDistance = traveledDistance;
			}
			
			//Send a message when motor rotation is done. This is needed to know when a scan is complete
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("MotorRotationDone 0");
				getExternalAccess().sendMessage(me);
			}
			
			//Send a message when there is a tachometer problem. TachoMeterProblem
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TachoMeterProblem");
				getExternalAccess().sendMessage(me);
			}
			//Send a message when a turn is complete, but only when it is correctly done. "TurnComplete"
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("TurnComplete");
				getExternalAccess().sendMessage(me);
			}
			//Send a message when a drive backwards is complete, but only when it is correctly done. "DriveBackwardComplete"
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("DriveBackwardComplete");
				getExternalAccess().sendMessage(me);
			}
			//Send a message for every sonar read. "SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}"
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}" );
				getExternalAccess().sendMessage(me);
			}
			
			//Send a message for light change from nothing to black or white and one for having nothing.
			//    "LightSensorStatus {relativeLightId} {nothing|black|white}"
			if(true){
				IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
				me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
				me.setContent("");
				getExternalAccess().sendMessage(me);
			}
		}
	}		
}
