package org.vu.advselforg.agentcontroller;

import java.io.Console;
import java.io.IOException;

import org.vu.advselforg.common.EMovingMode;
import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.robotcontroller.NxtBridge;
import org.vu.advselforg.robotcontroller.NxtController;
import org.vu.advselforg.robotcontroller.RobotController;

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
	boolean wasDrivingBackward = false;

	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		getBeliefbase().getBelief("WIMRunning").setFact(true);
		new Thread(this).start();
		waitFor(IFilter.NEVER);
	}
	// Mainline
	public void run() {
		try{
			while (true) {

				String[] sensorData = robot.RequestSensorData();
				System.out.println(sensorData[4]);
				processTouchSensor(sensorData[4]);
				//stepProcessing();			
				//processTachoMeterReding();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void processTurningUpdate(boolean isTurningOrDrivingBack, EMovingMode lastCommand) {
		if (!isTurningOrDrivingBack && lastCommand == EMovingMode.TURNING && wasTurning) {
			setBelief("turning", false);
			System.out.println("Turn completed.");
			wasTurning = false;
			//robot.ResetTravelDistance();
			oldTraveledDistance = 0;
			//robot.calibrateTurret(OutputPort.B);
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.TURNING && !wasTurning) {
			wasTurning = true;
		}
	}

	private void processDriveBackwardUpdate(boolean isTurningOrDrivingBack, EMovingMode lastCommand) {
		if (!isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && wasDrivingBackward) {
			setBelief("drivingBackwards", false);
			System.out.println("Driving backwards completed.");
			wasDrivingBackward = false;
			//robot.resetTravelDistance();
			oldTraveledDistance = 0;
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && !wasDrivingBackward) {
			wasDrivingBackward = true;
		}
	}

	private void processTachoMeterReding() {
		//if (!robot.atDesiredMotorSpeed()) {
		//	setBelief("tachometerProblem", true);
		//	System.out.println("Tachometer problem encountered.");
		//}
	}

	private void processTravelDistance() {
		//int traveledDistance = robot.getTravelDistance();
		//if ((traveledDistance - oldTraveledDistance) >= 1) {
		//	setBelief("distanceTraveled", traveledDistance);
		//	oldTraveledDistance = traveledDistance;
		//	System.out.println("Traveled " + traveledDistance + " cm.");
		//}
	}

	private void processMotorRotation() {
		//boolean isScanning = robot.isScanning(OutputPort.B);
		//if (wasScanning && !isScanning) {
		//	setBelief("scanningArea", false);
		//	System.out.println("Motor rotation completed.");
		//	robot.resetTravelDistance();
		//	oldTraveledDistance = 0;
		//	wasTurning = false;
		//}
		//if (!wasTurning && isScanning) {
		//	wasTurning = true;
		//}
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

	private void processTouchSensor(String value) {

		boolean touched = value.equals("1") ? true : false;
			
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
			//robot.calibrateTurret(OutputPort.B);
		}
	}
}