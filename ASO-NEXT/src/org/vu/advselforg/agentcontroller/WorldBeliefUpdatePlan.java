package org.vu.advselforg.agentcontroller;


import java.io.IOException;

import org.vu.advselforg.common.EMovingMode;

import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.SensorData;

import jadex.runtime.IFilter;
import jadex.runtime.Plan;

public class WorldBeliefUpdatePlan extends Plan implements Runnable {

	NxtBridge robot;
	int step = 0;

	boolean touchPressed = false;

	int oldTraveledDistance = 0;
	int oldTopSonarDistance = -1;
	int oldBottomSonarDistance = -1;
	
	boolean wasTurning = false;
	boolean wasScanning = false;
	boolean hasScanned = false;
	boolean wasDrivingBackward = false;
	SensorData sensorData; 
	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		new Thread(this).start();
		getBeliefbase().getBelief("WIMRunning").setFact(true);
		waitFor(IFilter.NEVER);
	}

	public void run() {

		while (true) {
			try {
				sensorData = robot.RequestSensorData();
				if(sensorData.isScanning() == false){
					if(hasScanned == true){
						//Process scanresult.
						processMotorRotation();
					}
					
					
					boolean isTurningOrMovingBackward = sensorData.isTurningOrMovingBackward();
					EMovingMode lastCommand = sensorData.lastCommand();
			
	
					processTurningUpdate(isTurningOrMovingBackward, lastCommand);
					processDriveBackwardUpdate(isTurningOrMovingBackward, lastCommand);
					processTouchSensor();
					processSonarSensor();
					processTravelDistance();
					
	
					
					stepProcessing();			
					//processTachoMeterReding();
				}else
				{
					hasScanned = true;
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processTurningUpdate(boolean isTurningOrDrivingBack, EMovingMode lastCommand) throws IOException {
		if (!isTurningOrDrivingBack && lastCommand == EMovingMode.TURNING && wasTurning) {
			setBelief("turning", false);
			System.out.println("Turn completed.");
			wasTurning = false;
			robot.ResetTrafeldistance();
			oldTraveledDistance = 0;
			//TODO CALIBRATE
			//robot.calibrateTurret(EMotorPort.B);
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.TURNING && !wasTurning) {
			wasTurning = true;
		}
	}

	private void processDriveBackwardUpdate(boolean isTurningOrDrivingBack, EMovingMode lastCommand) throws IOException {
		if (!isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && wasDrivingBackward) {
			setBelief("drivingBackwards", false);
			System.out.println("Driving backwards completed.");
			wasDrivingBackward = false;
			robot.ResetTrafeldistance();
			oldTraveledDistance = 0;
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && !wasDrivingBackward) {
			wasDrivingBackward = true;
			System.out.println("Still driving backward.");
		}
	}

	private void processTachoMeterReding() {
		if (!sensorData.atDesiredMotorSpeed()) {
			setBelief("tachometerProblem", true);
			System.out.println("Tachometer problem encountered.");
		}
	}

	private void processTravelDistance() {
		int traveledDistance = sensorData.getTravelDistance();
		//if ((traveledDistance - oldTraveledDistance) >= 1) {
			setBelief("distanceTraveled", traveledDistance);
			oldTraveledDistance = traveledDistance;
			System.out.println("Traveled " + traveledDistance + " cm.");
		//}
	}

	private void processMotorRotation() throws IOException {
	//	boolean isScanning = sensorData.isScanning();
	//	if (wasScanning && !isScanning) {
	//		setBelief("scanningArea", false);
	//		System.out.println("Motor rotation completed.");
	//		robot.ResetTrafeldistance();
	//		oldTraveledDistance = 0;
	//		wasTurning = false;
	//	}
	//	if (!wasTurning && isScanning) {
	//		wasTurning = true;
	//	}
	}

	private void processSonarSensor() {
		// Send a message for every sonar read.
		// "SonarSensorStatus {sonarRelativeID} {distance} {tachoMeterCount 0=default/middle}"
		/*
		 * int newTopDistance = robot.getDistance(0,
		 * DistanceMode.HIGHEST_NOT255); int newBottomDistance =
		 * robot.getDistance(1, DistanceMode.LOWEST); int
		 * tachoMeterCountSonarTurret = robot.getTachoMeterCount(OutputPort.B);
		 * 
		 * if(oldBottomSonarDistance == -1 || (Math.abs(newBottomDistance -
		 * oldBottomSonarDistance)) >= 1){ sendMessage("SonarSensorStatus 0 " +
		 * newBottomDistance +" " +tachoMeterCountSonarTurret );
		 * oldBottomSonarDistance = newBottomDistance; }
		 * 
		 * if(oldTopSonarDistance == -1 || (Math.abs(newBottomDistance -
		 * oldTopSonarDistance)) >= 1){ sendMessage("SonarSensorStatus 1 " +
		 * newTopDistance + " " + tachoMeterCountSonarTurret );
		 * oldTopSonarDistance = newTopDistance; }
		 */
	}

	private void processTouchSensor() {

		boolean touched = sensorData.getTouchSensorPressed();
		if (touched && !touchPressed) {
			setBelief("clusterDetected", true);
			System.out.println("Cluster detected.");
			touchPressed = true;
		}

		if (!touched && touchPressed) {
			setBelief("clusterDetected", false);
			System.out.println("Cluster released.");
			touchPressed = false;
		}
	}

	private void setBelief(String BeliefName, Object beliefValue) {
		try{
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
		getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
		}catch(Exception e){
			//sometimes I get an concurrent update error.. Jadex still works, but then I need to make sure that 
			//This belief still is updated.
			setBelief(BeliefName,beliefValue);
		}
		
	}

	private void stepProcessing() {
		// reset step
		step++;
		if (step == Integer.MAX_VALUE) {
			step = 0;
		}
		// Calibrate turret each 100 steps.
		if (step % 100 == 0) {
			//robot.calibrateTurret(EMotorPort.B);
		}
	}
}