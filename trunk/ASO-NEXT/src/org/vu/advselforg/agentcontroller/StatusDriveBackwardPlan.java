package org.vu.advselforg.agentcontroller;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusDriveBackwardPlan extends Plan{

	@Override
	public void body() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);

		getBeliefbase().getBelief("drivingBackwards").setFact(false);
		System.out.println("Driving backwards completed.");
		
	}

}
