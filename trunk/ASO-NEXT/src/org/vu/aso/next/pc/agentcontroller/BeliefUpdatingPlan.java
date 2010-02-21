package org.vu.aso.next.pc.agentcontroller;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.vu.aso.next.pc.NxtBridge;

import jadex.runtime.Plan;

public abstract class BeliefUpdatingPlan extends Plan {

	private static final long serialVersionUID = -809120734331923007L;
	protected SimpleDateFormat formatter;

	public BeliefUpdatingPlan() {
		formatter = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	protected NxtBridge getRobot() {
		return (NxtBridge) getBeliefbase().getBelief(Beliefs.ROBOT).getFact();
	}

	protected Object getBelief(String beliefName) {
		return getBeliefbase().getBelief(beliefName).getFact();
	}
	
	protected void setBelief(String beliefName, Object beliefValue) {
		getBeliefbase().getBelief(beliefName).setFact(beliefValue);
		printDebug("has value '" + beliefValue.toString() + "' for belief '" + beliefName + "'");
	}

	protected void printDebug(String message) {
		System.out.println(formatter.format(new Date()) + " " + (String) getBelief(Beliefs.ROBOT_NAME) + " " + message);
	}

}
