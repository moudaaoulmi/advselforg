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
	int oldTopSonarDistance =-1;
	int oldBottomSonarDistance =-1;
	
	boolean wasTurning = false;
	boolean wasDrivingBackward = false;
	
	int step = 0;
	
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
			
			if(step == Integer.MAX_VALUE){
				step =0;
			}
			
			if(step % 100 == 0){
				robot.calibrateTurret(2);
			}
			//Send a message when the touch sensor is pressed and it was previously not pressed.
			if(robot.getTouchSensorPressed(0) && !touchPressed){
				sendMessage("TouchSensorStatus " + "0 pressed");
				touchPressed = true;
			}
			//Send a message when the touch sensor is released and it was previously pressed.
			if(!robot.getTouchSensorPressed(0) && touchPressed){
				sendMessage("TouchSensorStatus " + "0 released");
				touchPressed = false;
			}
			
			//Send a message for every sonar read. "SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}"
			int newBottomDistance = robot.getDistance(0, DistanceMode.LOWEST);
			int newTopDistance = robot.getDistance(0, DistanceMode.HIGHEST_NOT255);
			int tachoMeterCountSonarTurret = robot.getTachoMeterCount(2); 
			
			if(oldBottomSonarDistance == -1 || (Math.abs(newBottomDistance - oldBottomSonarDistance)) >= 1){
				sendMessage("SonarSensorStatus 0 " + newBottomDistance +tachoMeterCountSonarTurret );
				oldBottomSonarDistance = newBottomDistance;
			}
			
			if(oldTopSonarDistance == -1 || (Math.abs(newBottomDistance - oldTopSonarDistance)) >= 1){
				sendMessage("SonarSensorStatus 1 " + newTopDistance +tachoMeterCountSonarTurret );
				oldTopSonarDistance = newTopDistance;
			}
			
			//Send a message when the robot moved 1 cm forward or when it is resetted to 0.
			int traveledDistance = robot.getTravelDistance();
			if((traveledDistance - oldTraveledDistance) >= 1 ){
				sendMessage("TraveledDistance " + traveledDistance);
				oldTraveledDistance = traveledDistance;
			}

			//Send a message when motor rotation is done. This is needed to know when a scan is complete
			if(true){
				sendMessage("MotorRotationDone 0");
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
			}

			
			//Send a message when there is a tachometer problem. TachoMeterProblem
			if(!robot.atDesiredMotorSpeed()){
				sendMessage("TachoMeterProblem");
			}
			//Send a message when a turn is complete, but only when it is correctly done. "TurnComplete"
			if(!robot.isTurning() && wasTurning){
				sendMessage("TurnComplete");
				wasTurning = false;
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
			}
			if(robot.isTurning() && !wasTurning){
				wasTurning = true;
			}
			//Send a message when a drive backwards is complete, but only when it is correctly done. "DriveBackwardComplete"
			if(!robot.isDrivingBackward() && wasDrivingBackward){
				sendMessage("DriveBackwardComplete");
				wasDrivingBackward = false;
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
			}
			if(robot.isDrivingBackward() && !wasDrivingBackward){
				wasDrivingBackward = true;
			}

			//Send a message for light change from nothing to black or white and one for having nothing.
			//    "LightSensorStatus {relativeLightId} {nothing|black|white}"
			if(false){
				sendMessage("");
			}
			
			step++;
		}
		
	}	
	private void sendMessage(String message){
		IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
		me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
		me.setContent(message);
		getExternalAccess().sendMessage(me);
	}
}

