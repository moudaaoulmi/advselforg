package org.vu.aso.next.pc.agentcontroller;

import jadex.runtime.Plan;

public abstract class BeliefUpdatingPlan extends Plan {

	private static final long serialVersionUID = -809120734331923007L;
    private String name;
	
	
	protected void setBelief(String BeliefName, Object beliefValue) {
		
		getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
	}
	
	protected void Debug(String Name, String Message){
		System.out.println(Name + " says " + Message);
		
	}

}
