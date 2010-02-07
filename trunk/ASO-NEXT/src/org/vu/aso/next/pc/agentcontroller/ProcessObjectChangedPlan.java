package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.ELightSensorValue;

public class ProcessObjectChangedPlan extends UpdatingPlan{

	private static final long serialVersionUID = -7333884474349273251L;

	@Override
	public void body() {
		// TODO Auto-generated method stub
		ELightSensorValue  lv = (ELightSensorValue) getBeliefbase().getBelief("objectInGripper").getFact();
		System.out.println("Blokje Detected " + lv);
	}

}
