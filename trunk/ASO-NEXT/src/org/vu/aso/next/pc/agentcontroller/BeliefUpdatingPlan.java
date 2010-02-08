package org.vu.aso.next.pc.agentcontroller;

import jadex.runtime.Plan;

public abstract class BeliefUpdatingPlan extends Plan {

	private static final long serialVersionUID = -809120734331923007L;
	
	protected void setBelief(String BeliefName, Object beliefValue) {
		getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
	}
	
	protected void printDebug(String message){
		System.out.println((String) getBeliefbase().getBelief("robotName").getFact() + " " + message);
	}

}
