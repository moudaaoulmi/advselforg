package org.vu.advselforg.agentcontroller;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusTachoMeterPlan extends Plan{

	@Override
	public void body() {

		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);

		getBeliefbase().getBelief("tachometerProblem").setFact(false);
		System.out.println("Tachometer problem encountered.");
	}

}
