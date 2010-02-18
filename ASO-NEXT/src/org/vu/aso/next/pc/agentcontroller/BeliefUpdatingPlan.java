package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.pc.NxtBridge;

import jadex.runtime.Plan;

public abstract class BeliefUpdatingPlan extends Plan {

	private static final long serialVersionUID = -809120734331923007L;

	protected NxtBridge getRobot() {
		return (NxtBridge) getBeliefbase().getBelief("robot").getFact();
	}

	protected Object getBelief(String BeliefName) {
		return getBeliefbase().getBelief(BeliefName).getFact();
	}
	
	protected void setBelief(String BeliefName, Object beliefValue) {
		getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
	}

	protected void printDebug(String message) {
		System.out.println((String) getBeliefbase().getBelief("robotName").getFact() + " " + message);
	}

}
