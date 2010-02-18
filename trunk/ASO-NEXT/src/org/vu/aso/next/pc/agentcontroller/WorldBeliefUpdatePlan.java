package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

import org.vu.aso.next.common.ELightSensorValue;
import org.vu.aso.next.common.EMovingMode;

import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

import jadex.runtime.IFilter;

public class WorldBeliefUpdatePlan extends BeliefUpdatingPlan implements Runnable {

	private static final long serialVersionUID = -7096221399333292349L;

	private static final String BELIEF_WIM_RUNNING = "WIMRunning";
	private static final String BELIEF_TURNING = "turning";
	private static final String BELIEF_DRIVING_BACKWARD = "drivingBackward";
	private static final String BELIEF_OBJECT_IN_GRIPPER = "objectInGripper";
	private static final String BELIEF_DISTANCE_TRAVELED = "distanceTraveled";
	private static final String BELIEF_CLOSEST_BLOCK_DISTANCE = "closestBlockDistance";
	private static final String BELIEF_CLOSEST_BLOCK_ANGLE = "distanceBlockAngle";
	private static final String BELIEF_TOP_SONAR_DISTANCE = "topSonarDistance";
	private static final String BELIEF_CLUSTER_DETECTED = "clusterDetected";
	
	private NxtBridge robot;
	private int step = 0;

	private int oldTravelDistance = 0;
	private ELightSensorValue oldLightValue = ELightSensorValue.NO_OBJECT;

	private boolean wasTurning = false;
	private boolean wasScanning = false;
	private boolean wasDrivingBackward = false;
	private boolean wasTouchPressed = false;

	private SensorData sensorData;

	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		new Thread(this).start();
		getBeliefbase().getBelief(BELIEF_WIM_RUNNING).setFact(true);
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

					processTurningUpdate();
					processDriveBackwardUpdate();
					processTouchSensor();
					processSonarSensor();
					processLightSensor();
					processTravelDistance();

					processSteps();
				} else {
					wasScanning = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processTurningUpdate() throws IOException {
		if (!sensorData.isTurningOrMovingBackward() && sensorData.lastCommand() == EMovingMode.TURNING && wasTurning) {
			setBelief(BELIEF_TURNING, false);
			printDebug("completed a turn");
			wasTurning = false;
			robot.resetTravelDistance();
			oldTravelDistance = 0;
		}
		if (sensorData.isTurningOrMovingBackward() && sensorData.lastCommand() == EMovingMode.TURNING && !wasTurning) {
			wasTurning = true;
		}
	}

	private void processDriveBackwardUpdate() throws IOException {
		if (!sensorData.isTurningOrMovingBackward() && sensorData.lastCommand() == EMovingMode.BACKWARD
				&& wasDrivingBackward) {
			setBelief(BELIEF_DRIVING_BACKWARD, false);
			printDebug("completed driving backward");
			wasDrivingBackward = false;
			robot.resetTravelDistance();
			oldTravelDistance = 0;
		}
		if (sensorData.isTurningOrMovingBackward() && sensorData.lastCommand() == EMovingMode.BACKWARD
				&& !wasDrivingBackward) {
			wasDrivingBackward = true;
		}
	}

	private void processLightSensor() {
		if (sensorData.getObjectType() != oldLightValue) {
			setBelief(BELIEF_OBJECT_IN_GRIPPER, sensorData.getObjectType());
			oldLightValue = sensorData.getObjectType();
		}
	}

	private void processTravelDistance() {
		int travelDistance = sensorData.getTravelDistance();
		if (travelDistance != oldTravelDistance) {
			setBelief(BELIEF_DISTANCE_TRAVELED, travelDistance);
			oldTravelDistance = travelDistance;
		}
	}

	private void processScanResults() throws IOException {
		setBelief(BELIEF_CLOSEST_BLOCK_DISTANCE, sensorData.getClosestblockDistance());
		setBelief(BELIEF_CLOSEST_BLOCK_ANGLE, sensorData.getClosestblockAngle());
	}

	private void processSonarSensor() {
		setBelief(BELIEF_TOP_SONAR_DISTANCE, sensorData.getDistanceUpperSonar());
	}

	private void processTouchSensor() {

		if (sensorData.getTouchSensorPressed() && !wasTouchPressed) {
			setBelief(BELIEF_CLUSTER_DETECTED, true);
			printDebug("detected a cluster");
			wasTouchPressed = true;
		}

		if (!sensorData.getTouchSensorPressed() && wasTouchPressed) {
			setBelief(BELIEF_CLUSTER_DETECTED, false);
			printDebug("released a cluster");
			wasTouchPressed = false;
		}
	}

	private void processSteps() throws IOException {
		step++;

		if (step == 100) {
			robot.calibrateTurret();
			step = 0;
		}
	}

	protected void setBelief(String BeliefName, Object beliefValue) {
		getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
	}

	protected void printDebug(String message) {
		System.out.println((String) getExternalAccess().getBeliefbase().getBelief("robotName").getFact() + " "
				+ message);
	}
}