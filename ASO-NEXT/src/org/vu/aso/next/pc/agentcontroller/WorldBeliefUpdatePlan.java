package org.vu.aso.next.pc.agentcontroller;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import org.vu.aso.next.common.EObjectType;

import org.vu.aso.next.pc.NxtBridge;
import org.vu.aso.next.pc.SensorData;

import jadex.runtime.IFilter;

public class WorldBeliefUpdatePlan extends BeliefUpdatingPlan implements Runnable {

	private static final boolean DEFAULT_PRINT_SETTING = false;
	private static final long MINUTES = 6000;
	private static final long EXPERIMENT_DURATION = 1 * MINUTES;
	private static final String ROBOT_NAMES = "JOEY\nCHANDLER\nROSS\nPatrick\n";
	private static final long serialVersionUID = -7096221399333292349L;

	private NxtBridge robot;
	private EObjectType oldObjectType = EObjectType.NO_OBJECT;
	private SensorData sensorData;

	private Date endTime;

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
		endTime = new Date(System.currentTimeMillis() + EXPERIMENT_DURATION);
		while (new Date().before(endTime)) {
			sensorData = robot.requestSensorData();

			// Read the sensor values
			processDriveForwardUpdate();
			processDriveBackwardUpdate();
			processTurningUpdate();
			processTouchSensor();
			processSonarSensor();
			processLightSensor();
			processTravelDistance();
		}
		exit();
	}

	private void processTurningUpdate() {
		if (!sensorData.isTurning() && (Boolean) getBelief(Beliefs.TURNING)) {
			setBelief(Beliefs.TURNING, false);
			if (DEFAULT_PRINT_SETTING)
				printDebug("completed a turn");
		}
	}

	private void processDriveBackwardUpdate() {
		if (!sensorData.isDrivingBackward() && (Boolean) getBelief(Beliefs.DRIVING_BACKWARD)) {
			setBelief(Beliefs.DRIVING_BACKWARD, false);
			if (DEFAULT_PRINT_SETTING)
				printDebug("completed driving backward");
		}
	}

	private void processDriveForwardUpdate() {
		if (!sensorData.isDrivingForward() && (Boolean) getBelief(Beliefs.DRIVING_FORWARD)) {
			setBelief(Beliefs.DRIVING_FORWARD, false);
			if (DEFAULT_PRINT_SETTING)
				printDebug("completed driving forward");
		}
	}

	private void processLightSensor() {
		if (sensorData.getObjectType() != oldObjectType) {
			setBelief(Beliefs.OBJECT_IN_GRIPPER, sensorData.getObjectType(), true);
			oldObjectType = sensorData.getObjectType();
		}
	}

	private void processTravelDistance() {
		if (sensorData.getTravelDistance() != (Integer) getBelief(Beliefs.DISTANCE_TRAVELED)) {
			setBelief(Beliefs.DISTANCE_TRAVELED, sensorData.getTravelDistance(), false);
		}
	}

	private void processSonarSensor() {
		setBelief(Beliefs.TOP_SONAR_DISTANCE, sensorData.getDistanceUpperSonar(), false);
	}

	private void processTouchSensor() {
		if (sensorData.getTouchSensorPressed() && !(Boolean) getBelief(Beliefs.CLUSTER_DETECTED)) {
			setBelief(Beliefs.CLUSTER_DETECTED, true);
			if (DEFAULT_PRINT_SETTING)
				printDebug("detected a cluster");
		}

		if (!sensorData.getTouchSensorPressed() && (Boolean) getBelief(Beliefs.CLUSTER_DETECTED)) {
			setBelief(Beliefs.CLUSTER_DETECTED, false);
			if (DEFAULT_PRINT_SETTING)
				printDebug("released a cluster");
		}
	}

	private void exit() {
		try {
			writeNamesToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		robot.exit();
		getExternalAccess().killAgent();
		System.exit(0);
	}

	private void writeNamesToFile() throws FileNotFoundException {
		PrintStream out;
		out = new PrintStream("C:\\names.txt");
		out.print(ROBOT_NAMES);
		out.close();
	}

	@Override
	protected Object getBelief(String beliefName) {
		return getExternalAccess().getBeliefbase().getBelief(beliefName).getFact();
	}

	@Override
	protected void setBelief(String beliefName, Object beliefValue) {
		setBelief(beliefName, beliefValue, DEFAULT_PRINT_SETTING);
	}

	@Override
	protected void setBelief(String beliefName, Object beliefValue, boolean printDebug) {
		getExternalAccess().getBeliefbase().getBelief(beliefName).setFact(beliefValue);
		if (printDebug)
			printDebug("has value '" + beliefValue.toString() + "' for belief '" + beliefName + "'");
	}

	@Override
	protected void printDebug(String message) {
		System.out.println(formatter.format(new Date()) + " " + (String) getBelief(Beliefs.ROBOT_NAME) + ":WIM "
				+ message);
	}
}