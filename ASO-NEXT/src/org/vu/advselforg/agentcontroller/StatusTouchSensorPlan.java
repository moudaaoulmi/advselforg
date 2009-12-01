package org.vu.advselforg.agentcontroller;

import java.util.StringTokenizer;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusTouchSensorPlan extends Plan{

	@Override
	public void body() {
		// TODO Auto-generated method stub
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);
		StringTokenizer st = new StringTokenizer(data);
		
		String messageType = (String) st.nextElement();
		int relativePortNumber = Integer.parseInt((String) st.nextElement());
		String status = (String) st.nextElement();
		
		if(status.equals("pressed")){
			getBeliefbase().getBelief("clusterDetected").setFact(true);
			System.out.println("Cluster detected.");
		}else{
			getBeliefbase().getBelief("clusterDetected").setFact(false);
			System.out.println("Cluster released.");
		}
	}

}
