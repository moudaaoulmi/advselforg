package org.vu.aso.next.pc.agentcontroller;

import java.util.Random;

import jadex.runtime.Plan;

public abstract class UpdatingPlan extends Plan {

	private static final long serialVersionUID = -809120734331923007L;

	protected void setBelief(String BeliefName, Object beliefValue) {
		try {
			Thread.sleep(new Random().nextInt(20));
			getExternalAccess().getBeliefbase().getBelief(BeliefName).setFact(beliefValue);
			getExternalAccess().getBeliefbase().getBelief(BeliefName).modified();
		} catch (Exception e) {
			e.printStackTrace();
			// sometimes I get an concurrent update error.. Jadex still works,
			// but then I need to make sure that
			// this belief still is updated.
			try {
				Thread.sleep(new Random().nextInt(20));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setBelief(BeliefName, beliefValue);
		}
	}

}
