package org.vu.advselforg.agentcontroller;

import java.util.StringTokenizer;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusTravelDistancePlan extends Plan{

	@Override
	public void body() {
		
			
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		
		String data = em.getContent().toString();
		System.out.println("received "+data);
		StringTokenizer st = new StringTokenizer(data);
		
		String messageType = (String) st.nextElement();
		int distance = Integer.parseInt((String) st.nextElement());
		
		
		getBeliefbase().getBelief("distanceTraveled").setFact(distance);
		System.out.println("Traveled " + distance + " cm.");
		}		
		
	

}
