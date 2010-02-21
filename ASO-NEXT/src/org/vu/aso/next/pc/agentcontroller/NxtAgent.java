package org.vu.aso.next.pc.agentcontroller;

import java.text.SimpleDateFormat;
import java.util.Date;

import jadex.runtime.IExternalAccess;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

public class NxtAgent extends NxtBridge {
	
	protected SimpleDateFormat formatter;
	IExternalAccess ea;

	public NxtAgent(String nxtName, ESensorType port1, boolean monitorPort1, ESensorType port2, boolean monitorPort2,
			ESensorType port3, boolean monitorPort3, ESensorType port4, boolean monitorPort4, EMotorPort pilotPortLeft,
			EMotorPort pilotPortRight, Boolean MotorReverse, float wheelDiameter, float trackWidth, IExternalAccess ea) {
		super(nxtName, port1, monitorPort1, port2, monitorPort2, port3, monitorPort3, port4, monitorPort4, pilotPortLeft,
				pilotPortRight, MotorReverse, wheelDiameter, trackWidth);
		this.ea = ea;
	}

	@Override
	public void driveForward(int distance) {
		super.driveForward(distance);
		setBelief(Beliefs.DRIVING_FORWARD, true);
	}
	
	@Override
	public void driveBackward(int distance) {
		super.driveForward(distance);
		setBelief(Beliefs.DRIVING_BACKWARD, true);
	}
	
	@Override
	public void turnLeft(int angle) {
		super.turnLeft(angle);
		setBelief(Beliefs.TURNING, true);
	}
	
	@Override
	public void turnRight(int angle) {
		super.turnRight(angle);
		setBelief(Beliefs.TURNING, true);
	}
	
	@Override
	public void performScan(int fromAngle, int toAngle) {
		super.performScan(fromAngle, toAngle);
		setBelief(Beliefs.SCANNING, true);
	}
	
	protected Object getBelief(String beliefName) {
		return ea.getBeliefbase().getBelief(beliefName).getFact();
	}
	
	protected void setBelief(String beliefName, Object beliefValue) {
		ea.getBeliefbase().getBelief(beliefName).setFact(beliefValue);
		printDebug("has value '" + beliefValue.toString() + "' for belief '" + beliefName + "'");
	}
	
	protected void printDebug(String message) {
		System.out.println(formatter.format(new Date()) + " " + (String) getBelief(Beliefs.ROBOT_NAME) + " " + message);
	}
}
