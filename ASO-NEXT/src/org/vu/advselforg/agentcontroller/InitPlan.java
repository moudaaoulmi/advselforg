package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.robotcontroller.*;

import sun.awt.EmbeddedFrame;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.IFilter;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class InitPlan extends Plan{

	private static final long serialVersionUID = 2961883448970106007L;
	NxtController robot;
	String agentName;
	
	boolean touchPressed = false;
	
	int oldTraveledDistance = 0;
	int oldTopSonarDistance =-1;
	int oldBottomSonarDistance =-1;
	
	boolean wasTurning = false;
	boolean wasScanning = false;
	boolean wasDrivingBackward = false;
	
	int step = 0;
	
	public InitPlan(String agentName){
		this.agentName = agentName;
	}
	
	
	@Override
	public void body() {
			
		OutputPort leftMotorPort = OutputPort.A;
		OutputPort rightMotorPort = OutputPort.C;
		boolean motorReverse = false;
		SensorType port1 = SensorType.ULTRASONIC;
		SensorType port2 = SensorType.ULTRASONIC;
		SensorType port3 = SensorType.LIGHT;
		SensorType port4 = SensorType.TOUCH;
		try {
			robot = new NxtController("JOEY", leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);
		
			
		getBeliefbase().getBelief("robot").setFact(robot);
		System.out.println("Connected");
			
		getBeliefbase().getBelief("Initialized").setFact(true);
		} catch (Exception e) {
			System.out.println("Whoops, does not compute. Cannot connect to robot.");
		}
	}

	
	public void run() {
		//while(true){
			/*
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
				step = 0;
			}
			
			if(step % 100 == 0){
				robot.calibrateTurret(OutputPort.B);
			}
			//Send a message for every sonar read. "SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}"
			int newTopDistance = robot.getDistance(0, DistanceMode.HIGHEST_NOT255);
			int newBottomDistance = robot.getDistance(1, DistanceMode.LOWEST);
			int tachoMeterCountSonarTurret = robot.getTachoMeterCount(OutputPort.B); 
			
			if(oldBottomSonarDistance == -1 || (Math.abs(newBottomDistance - oldBottomSonarDistance)) >= 1){
				sendMessage("SonarSensorStatus 0 " + newBottomDistance +" " +tachoMeterCountSonarTurret );
				oldBottomSonarDistance = newBottomDistance;
			}
			
			if(oldTopSonarDistance == -1 || (Math.abs(newBottomDistance - oldTopSonarDistance)) >= 1){
				sendMessage("SonarSensorStatus 1 " + newTopDistance + " " + tachoMeterCountSonarTurret );
				oldTopSonarDistance = newTopDistance;
			}
			
			
			boolean touched = robot.getTouchSensorPressed(0); 
			//Send a message when the touch sensor is pressed and it was previously not pressed.
			if(touched && !touchPressed){
				sendMessage("TouchSensorStatus " + "0 pressed");
				touchPressed = true;
			}
			//Send a message when the touch sensor is released and it was previously pressed.
			if(!touched && touchPressed){
				sendMessage("TouchSensorStatus " + "0 released");
				touchPressed = false;
			}
			

			
			//Send a message when the robot moved 1 cm forward or when it is resetted to 0.
			int traveledDistance = robot.getTravelDistance();
			//if((traveledDistance - oldTraveledDistance) >= 1 ){
				sendMessage("TraveledDistance " + traveledDistance);
			//	oldTraveledDistance = traveledDistance;
			//}
			boolean isMoving = robot.isMoving();
			MovingMode lastCommand = robot.lastCommand();
			
			//Send a message when motor rotation is done. This is needed to know when a scan is complete
			boolean isScanning = robot.isScanning(OutputPort.B);  
			if( wasScanning && isScanning){
				sendMessage("MotorRotationDone 0");
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
				wasTurning = false;
			}
			if(!wasTurning && isScanning){
				wasTurning=true;
			}
			
			
			//Send a message when there is a tachometer problem. TachoMeterProblem
			if(!robot.atDesiredMotorSpeed()){
				sendMessage("TachoMeterProblem");
			}
			
			
			//Send a message when a turn is complete, but only when it is correctly done. "TurnComplete"
			if(!isMoving && lastCommand == MovingMode.TURNING  && wasTurning){
				sendMessage("TurnComplete");
				wasTurning = false;
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
				robot.calibrateTurret(OutputPort.B);
			}
			if(isMoving && lastCommand == MovingMode.TURNING && !wasTurning){
				wasTurning = true;
			}
			
			//Send a message when a drive backwards is complete, but only when it is correctly done. "DriveBackwardComplete"
			if(!isMoving && lastCommand == MovingMode.BACKWARD && wasDrivingBackward){
				sendMessage("DriveBackwardComplete");
				wasDrivingBackward = false;
				robot.resetTravelDistance();
				oldTraveledDistance = 0;
			}
			if(isMoving && lastCommand == MovingMode.BACKWARD && !wasDrivingBackward){
				wasDrivingBackward = true;
			}
			/*
			//Send a message for light change from nothing to black or white and one for having nothing.
			//    "LightSensorStatus {relativeLightId} {nothing|black|white}"
			if(false){
				sendMessage("");
			}
	
			step++;
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		//}
		
	}	
	private void sendMessage(String message){
		IMessageEvent me = getExternalAccess().createMessageEvent("MessageSend");
		me.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(agentName,true));
		me.setContent(message);
		getExternalAccess().sendMessage(me);
	}
}

