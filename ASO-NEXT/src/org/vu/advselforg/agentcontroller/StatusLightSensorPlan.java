package org.vu.advselforg.agentcontroller;

import java.util.StringTokenizer;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusLightSensorPlan extends Plan{

	@Override
	public void body() {
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);
		StringTokenizer st = new StringTokenizer(data);
		
		String messageType = (String) st.nextElement();
		int relativePortNumber = Integer.parseInt((String) st.nextElement());
		String status = (String) st.nextElement();
		
		if(status.equals("nothing")){
			getBeliefbase().getBelief("objectInGripper").setFact("nothing");
			System.out.println("No object in gripper.");
		}else if(status.equals("black")){
			getBeliefbase().getBelief("objectInGripper").setFact("black");
			System.out.println("Black object in gripper.");
		}else
		{
			getBeliefbase().getBelief("objectInGripper").setFact("white");
			System.out.println("White object in gripper.");
		}
		
	}

}
