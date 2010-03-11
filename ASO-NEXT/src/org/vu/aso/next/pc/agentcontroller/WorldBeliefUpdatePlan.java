package org.vu.aso.next.pc.agentcontroller;

import java.util.Date;

import org.vu.aso.next.common.EObjectType;

import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

import jadex.runtime.IFilter;

public class WorldBeliefUpdatePlan extends BeliefUpdatingPlan implements Runnable {

	private static final long serialVersionUID = -7096221399333292349L;

	private NxtBridge robot;
	private EObjectType oldObjectType = EObjectType.NO_OBJECT;
	private SensorData sensorData;

	public void body() {
		initialize();
	}

	private void initialize() {
		robot = (NxtBridge) getBeliefbase().getBelief(Beliefs.ROBOT).getFact();
		new Thread(this).start();
		getBeliefbase().getBelief(Beliefs.WIM_RUNNING).setFact(true);
		getBeliefbase().getBelief(Beliefs.READY_FOR_COMMAND).setFact(true);
		waitFor(IFilter.NEVER);
	}

	public void run() {
		while (true) {
			sensorData = robot.requestSensorData();

			// Read the sensor values
			processDriveForwardUpdate();
			processDriveBackwardUpdate();
			processTurningUpdate();
			proccesReadyForCommand();
			processTouchSensor();
			processSonarSensor();
			processLightSensor();
			processTravelDistance();

			printDebug("processed sensor data");
		}
	}

	private void processTurningUpdate() {
		if (!sensorData.isTurning() && (Boolean) getBelief(Beliefs.TURNING)) {
			setBelief(Beliefs.TURNING, false);
			printDebug("completed a turn");
		}
	}

	private void processDriveBackwardUpdate() {
		if (!sensorData.isDrivingBackward() && (Boolean) getBelief(Beliefs.DRIVING_BACKWARD)) {
			setBelief(Beliefs.DRIVING_BACKWARD, false);
			printDebug("completed driving backward");
		}
	}

	private void processDriveForwardUpdate() {
		if (!sensorData.isDrivingForward() && (Boolean) getBelief(Beliefs.DRIVING_FORWARD)) {
			setBelief(Beliefs.DRIVING_FORWARD, false);
			printDebug("completed driving forward");
		}
	}

	private void proccesReadyForCommand() {
		if (!sensorData.isMoving()) {
			setBelief(Beliefs.READY_FOR_COMMAND, true);
		}
	}

	private void processLightSensor() {
		if (sensorData.getObjectType() != oldObjectType) {
			setBelief(Beliefs.OBJECT_IN_GRIPPER, sensorData.getObjectType());
			oldObjectType = sensorData.getObjectType();
		}
	}

	private void processTravelDistance() {
		if (sensorData.getTravelDistance() != (Integer) getBelief(Beliefs.DISTANCE_TRAVELED)) {
			setBelief(Beliefs.DISTANCE_TRAVELED, sensorData.getTravelDistance());
		}
	}

	private void processSonarSensor() {
		setBelief(Beliefs.OLD_TOP_SONAR_DISTANCE, getBelief(Beliefs.TOP_SONAR_DISTANCE));
		setBelief(Beliefs.TOP_SONAR_DISTANCE, sensorData.getDistanceUpperSonar());
	}

	private void processTouchSensor() {
		if (sensorData.getTouchSensorPressed() && !sensorData.wasTouchPressed) {
			setBelief(Beliefs.CLUSTER_DETECTED, true);
			printDebug("detected a cluster");
			sensorData.wasTouchPressed = true;
		}

		if (!sensorData.getTouchSensorPressed() && sensorData.wasTouchPressed) {
			setBelief(Beliefs.CLUSTER_DETECTED, false);
			printDebug("released a cluster");
			sensorData.wasTouchPressed = false;
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
		System.out.println(formatter.format(new Date()) + " "
				+ (String) getBelief(Beliefs.ROBOT_NAME) + ":WIM "
				+ message);
	}
}