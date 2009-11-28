package org.vu.advselforg.agentcontroller;

import java.util.StringTokenizer;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusMotorRotationPlan extends Plan{

	@Override
	public void body() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);
			
		getBeliefbase().getBelief("scanningArea").setFact(false);
		System.out.println("Motor rotation completed.");
		
	}

}
