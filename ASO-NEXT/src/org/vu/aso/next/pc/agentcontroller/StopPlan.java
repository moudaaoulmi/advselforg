package org.vu.aso.next.pc.agentcontroller;

public class StopPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6604377277788735724L;

	@Override
	public void body() {
		printDebug("executed StopPlan()");
		getRobot().stop();
	}

}
