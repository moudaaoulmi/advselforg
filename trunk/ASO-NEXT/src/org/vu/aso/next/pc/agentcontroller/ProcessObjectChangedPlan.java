package org.vu.aso.next.pc.agentcontroller;

import org.vu.aso.next.common.EObjectType;

public class ProcessObjectChangedPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -7333884474349273251L;

	@Override
	public void body() {
		EObjectType lv = (EObjectType) getBeliefbase().getBelief("objectInGripper").getFact();
		printDebug("detected an object " + lv);
	}

}
