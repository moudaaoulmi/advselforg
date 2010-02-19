package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;
import java.util.Date;

import org.vu.aso.next.common.EObjectType;

import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

import jadex.runtime.IFilter;

public class WorldBeliefUpdatePlan extends BeliefUpdatingPlan implements Runnable {

	private static final long serialVersionUID = -7096221399333292349L;

	private static final String BELIEF_WIM_RUNNING = "WIMRunning";
	private static final String BELIEF_READY_FOR_COMMAND = "readyForCommand";
	private static final String BELIEF_TURNING = "turning";
	private static final String BELIEF_DRIVING_BACKWARD = "drivingBackward";
	private static final String BELIEF_DRIVING_FORWARD = "drivingForward";
	private static final String BELIEF_OBJECT_IN_GRIPPER = "objectInGripper";
	private static final String BELIEF_DISTANCE_TRAVELED = "distanceTraveled";
	private static final String BELIEF_CLOSEST_BLOCK_DISTANCE = "closestBlockDistance";
	private static final String BELIEF_CLOSEST_BLOCK_ANGLE = "distanceBlockAngle";
	private static final String BELIEF_TOP_SONAR_DISTANCE = "topSonarDistance";
	private static final String BELIEF_OLD_TOP_SONAR_DISTANCE = "oldTopSonarDistance";
	private static final String BELIEF_CLUSTER_DETECTED = "clusterDetected";

	private NxtBridge robot;
	private int step = 0;

	private int oldTravelDistance = 0;
	private EObjectType oldObjectType = EObjectType.NO_OBJECT;

	private boolean wasTurning = false;
	private boolean wasScanning = false;
	private boolean wasDrivingBackward = false;
	private boolean wasDrivingForward = false;
	private boolean wasTouchPressed = false;

	private SensorData sensorData;

	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (NxtBridge) getBeliefbase().getBelief("robot").getFact();
		new Thread(this).start();
		getBeliefbase().getBelief(BELIEF_WIM_RUNNING).setFact(true);
		getBeliefbase().getBelief(BELIEF_READY_FOR_COMMAND).setFact(true);
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
					processDriveForwardUpdate();
					processTouchSensor();
					if(!sensorData.isTurning()){
					      processSonarSensor();
					}
					processLightSensor();
					processTravelDistance();

					processSteps();
				} else {
					wasScanning = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			printDebug("processed sensor data");
		}
	}

	private void processTurningUpdate() throws IOException {
		if (sensorData.isTurning() && !wasTurning) {
			wasTurning = true;
		}

		if (!sensorData.isTurning() && wasTurning) {
			setBelief(BELIEF_TURNING, false);
			printDebug("completed a turn");
			wasTurning = false;

			robot.resetTravelDistance();
			oldTravelDistance = 0;
		}
	}

	private void processDriveBackwardUpdate() throws IOException {
		if (sensorData.isMovingBackward() && !wasDrivingBackward) {
			wasDrivingBackward = true;
		}

		if (!sensorData.isMovingBackward() && wasDrivingBackward) {
			setBelief(BELIEF_DRIVING_BACKWARD, false);
			printDebug("completed driving backward");
			wasDrivingBackward = false;

			robot.resetTravelDistance();
			oldTravelDistance = 0;
		}
	}

	private void processDriveForwardUpdate() throws IOException {
		if (sensorData.isMovingForward() && !wasDrivingForward) {
			wasDrivingForward = true;
		}

		if (!sensorData.isMovingForward() && wasDrivingForward) {
			setBelief(BELIEF_DRIVING_FORWARD, false);
			printDebug("completed driving forward");
			wasDrivingForward = false;

			robot.resetTravelDistance();
			oldTravelDistance = 0;
		}
	}

	private void processLightSensor() {
		if (sensorData.getObjectType() != oldObjectType) {
			setBelief(BELIEF_OBJECT_IN_GRIPPER, sensorData.getObjectType());
			oldObjectType = sensorData.getObjectType();
		}
	}

	private void processTravelDistance() {
		if (sensorData.getTravelDistance() != oldTravelDistance) {
			setBelief(BELIEF_DISTANCE_TRAVELED, sensorData.getTravelDistance());
			oldTravelDistance = sensorData.getTravelDistance();
		}
	}

	private void processScanResults() throws IOException {
		setBelief(BELIEF_CLOSEST_BLOCK_DISTANCE, sensorData.getClosestblockDistance());
		setBelief(BELIEF_CLOSEST_BLOCK_ANGLE, sensorData.getClosestblockAngle());
	}

	private void processSonarSensor() {
		setBelief(BELIEF_OLD_TOP_SONAR_DISTANCE, getBelief(BELIEF_TOP_SONAR_DISTANCE));
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

	@Override
	protected Object getBelief(String beliefName) {
		return getExternalAccess().getBeliefbase().getBelief(beliefName).getFact();
	}

	@Override
	protected void setBelief(String beliefName, Object beliefValue) {
		getExternalAccess().getBeliefbase().getBelief(beliefName).setFact(beliefValue);
		printDebug("has value '" + beliefValue.toString() + "' for belief '" + beliefName + "'");
	}

	@Override
	protected void printDebug(String message) {
		System.out.println(formatter.format(new Date()) + " " + (String) getExternalAccess().getBeliefbase().getBelief("robotName").getFact() + ":WIM "
				+ message);
	}
}