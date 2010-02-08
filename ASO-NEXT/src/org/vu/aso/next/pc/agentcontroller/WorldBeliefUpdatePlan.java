package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

import org.vu.aso.next.common.ELightSensorValue;
import org.vu.aso.next.common.EMovingMode;

import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

import jadex.runtime.IFilter;
import jadex.runtime.Plan;

public class WorldBeliefUpdatePlan extends Plan implements Runnable {

	private static final long serialVersionUID = 1L;
	NxtBridge robot;
	int step = 0;

	boolean touchPressed = false;

	int oldTraveledDistance = 0;
	int oldTopSonarDistance = -1;
	int oldBottomSonarDistance = -1;

	boolean wasTurning = false;
	ELightSensorValue oldLightValue = ELightSensorValue.NO_OBJECT;
	boolean wasScanning = false;
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
				sensorData = robot.requestSensorData();
				if (sensorData.isScanning() == false) {
					if (wasScanning == true) {
						processScanResults();
						wasScanning = false;
					}

					boolean isTurningOrMovingBackward = sensorData.isTurningOrMovingBackward();
					EMovingMode lastCommand = sensorData.lastCommand();

					processTurningUpdate(isTurningOrMovingBackward, lastCommand);
					processDriveBackwardUpdate(isTurningOrMovingBackward, lastCommand);
					processTouchSensor();
					//processSonarSensor();
					processLightSensor();
					//processTravelDistance();

					//stepProcessing();
				} else {
					wasScanning = true;
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
			robot.resetTravelDistance();
			oldTraveledDistance = 0;
			// TODO CALIBRATE
			// robot.calibrateTurret(EMotorPort.B);
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.TURNING && !wasTurning) {
			wasTurning = true;
		}
	}

	private void processDriveBackwardUpdate(boolean isTurningOrDrivingBack, EMovingMode lastCommand) throws IOException {
		if (!isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && wasDrivingBackward) {
			setBelief("drivingBackward", false);
			System.out.println("Driving backwards completed.");
			wasDrivingBackward = false;
			robot.resetTravelDistance();
			oldTraveledDistance = 0;
		}
		if (isTurningOrDrivingBack && lastCommand == EMovingMode.BACKWARD && !wasDrivingBackward) {
			wasDrivingBackward = true;
			System.out.println("Still driving backward.");
		}
	}

	private void processLightSensor(){
		if(sensorData.getObjectType() != oldLightValue){
			setBelief("objectInGripper", sensorData.getObjectType());
			oldLightValue = sensorData.getObjectType();
		}	
	}
	
	private void processTravelDistance() {
		int traveledDistance = sensorData.getTravelDistance();
		// if ((traveledDistance - oldTraveledDistance) >= 1) {
		setBelief("distanceTraveled", traveledDistance);
		oldTraveledDistance = traveledDistance;
		System.out.println("Traveled " + traveledDistance + " cm.");
		// }
	}

	private void processScanResults() throws IOException {
		setBelief("closestBlockDistance", sensorData.getClosestblockDistance());
		setBelief("distanceBlockAngle", sensorData.getClosestblockAngle());	
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
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
	}

	private void stepProcessing() {
		// reset step
		step++;
		if (step == Integer.MAX_VALUE) {
			step = 0;
		}
		// Calibrate turret each 100 steps.
		if (step % 100 == 0) {
			// robot.calibrateTurret(EMotorPort.B);
		}
	}
	
	public void finalize(){
		try {
			robot.exit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}