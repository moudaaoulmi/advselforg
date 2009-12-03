package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.robotcontroller.MovingMode;
import org.vu.advselforg.robotcontroller.NxtController;
import org.vu.advselforg.robotcontroller.OutputPort;
import org.vu.advselforg.robotcontroller.RobotController;

import jadex.runtime.IFilter;
import jadex.runtime.Plan;

public class WorldBeliefUpdatePlan extends Plan implements Runnable {

	RobotController robot;
	int step = 0;

	boolean touchPressed = false;

	int oldTraveledDistance = 0;
	int oldTopSonarDistance = -1;
	int oldBottomSonarDistance = -1;

	boolean wasTurning = false;
	boolean wasScanning = false;
	boolean wasDrivingBackward = false;

	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (RobotController) getBeliefbase().getBelief("robot").getFact();
		getBeliefbase().getBelief("WIMRunning").setFact(true);
		new Thread(this).start();
		waitFor(IFilter.NEVER);
	}
	boolean wasTurningOrDrivingBack = false;
	boolean performExtraProcessStep = false;

	// Mainline
	public void run() {
		while (true) {

			boolean isTurningOrDrivingBack = robot.isTurningOrDrivingBack();
			MovingMode lastCommand = robot.lastCommand();
			if(step != 0){
				if(!isTurningOrDrivingBack && wasTurningOrDrivingBack){
					performExtraProcessStep = true;
				}
			}
			
			if(isTurningOrDrivingBack || performExtraProcessStep){
				if(!performExtraProcessStep){
					wasTurningOrDrivingBack = true;
				}else{
					wasTurningOrDrivingBack = false;
				}
				processTurningUpdate(isTurningOrDrivingBack, lastCommand);
				processDriveBackwardUpdate(isTurningOrDrivingBack, lastCommand);
				System.out.println("AAAAAAAAAA");
				performExtraProcessStep = false;
				
			}else{
				processTouchSensor();
				processSonarSensor();
				processTravelDistance();
				processMotorRotation();
				System.out.println("BBBBBBBBB");
				wasTurningOrDrivingBack = false;
				
			}
			stepProcessing();			
			//processTachoMeterReding();

		}
	}

	private void processTurningUpdate(boolean isTurningOrDrivingBack, MovingMode lastCommand) {
		if (!isTurningOrDrivingBack && lastCommand == MovingMode.TURNING && wasTurning) {
			setBelief("turning", false);
			System.out.println("Turn completed.");
			wasTurning = false;
			robot.resetTravelDistance();
			oldTraveledDistance = 0;
			robot.calibrateTurret(OutputPort.B);
		}
		if (isTurningOrDrivingBack && lastCommand == MovingMode.TURNING && !wasTurning) {
			wasTurning = true;
		}
	}

	private void processDriveBackwardUpdate(boolean isTurningOrDrivingBack, MovingMode lastCommand) {
		if (!isTurningOrDrivingBack && lastCommand == MovingMode.BACKWARD && wasDrivingBackward) {
			setBelief("drivingBackwards", false);
			System.out.println("Driving backwards completed.");
			wasDrivingBackward = false;
			robot.resetTravelDistance();
			oldTraveledDistance = 0;
		}
		if (isTurningOrDrivingBack && lastCommand == MovingMode.BACKWARD && !wasDrivingBackward) {
			wasDrivingBackward = true;
		}
	}

	private void processTachoMeterReding() {
		if (!robot.atDesiredMotorSpeed()) {
			setBelief("tachometerProblem", true);
			System.out.println("Tachometer problem encountered.");
		}
	}

	private void processTravelDistance() {
		int traveledDistance = robot.getTravelDistance();
		//if ((traveledDistance - oldTraveledDistance) >= 1) {
			setBelief("distanceTraveled", traveledDistance);
			oldTraveledDistance = traveledDistance;
			System.out.println("Traveled " + traveledDistance + " cm.");
		//}
	}

	private void processMotorRotation() {
		boolean isScanning = robot.isScanning(OutputPort.B);
		if (wasScanning && !isScanning) {
			setBelief("scanningArea", false);
			System.out.println("Motor rotation completed.");
			robot.resetTravelDistance();
			oldTraveledDistance = 0;
			wasTurning = false;
		}
		if (!wasTurning && isScanning) {
			wasTurning = true;
		}
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

		boolean touched = robot.getTouchSensorPressed(0);
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
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
		getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
	}

	private void stepProcessing() {
		// reset step
		step++;
		if (step == Integer.MAX_VALUE) {
			step = 0;
		}
		// Calibrate turret each 100 steps.
		if (step % 100 == 0) {
			robot.calibrateTurret(OutputPort.B);
		}
	}
}