package org.vu.aso.next.pc.agentcontroller;

import java.io.Console;

import org.vu.aso.next.common.ELightSensorValue;

import jadex.runtime.Plan;

public class ProcessObjectChangedPlan extends Plan{

	@Override
	public void body() {
		// TODO Auto-generated method stub
		ELightSensorValue  lv = (ELightSensorValue) getBeliefbase().getBelief("objectInGripper").getFact();
		System.out.println("Blokje Detected " + lv);
	}

}
