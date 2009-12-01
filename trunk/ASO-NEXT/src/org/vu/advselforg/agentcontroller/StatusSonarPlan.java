package org.vu.advselforg.agentcontroller;

import java.util.StringTokenizer;

import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class StatusSonarPlan extends Plan{

	@Override
	public void body() {
		IMessageEvent em = (IMessageEvent)getInitialEvent();
		String data = em.getContent().toString();
		System.out.println("received "+data);
		StringTokenizer st = new StringTokenizer(data);
		
		String messageType = (String) st.nextElement();
		int relativePortNumber = Integer.parseInt((String)st.nextElement());
		int status = Integer.parseInt((String)st.nextElement());
		int tachoMeterCount = Integer.parseInt((String) st.nextElement());
		
		
		if(relativePortNumber == 0){
			getBeliefbase().getBelief("topSonarDistance").setFact(status);
			System.out.println("Top sonar distance :" + status);
		}else{
			getBeliefbase().getBelief("bottomSonarDistance").setFact(status);
			//We need to test this part
			System.out.println("Bottom sonar distance :" + status);
		}
		getBeliefbase().getBelief("sonarAngle").setFact(tachoMeterCount);
		
	}

}
