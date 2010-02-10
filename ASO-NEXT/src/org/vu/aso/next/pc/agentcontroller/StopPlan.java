package org.vu.aso.next.pc.agentcontroller;

import java.io.IOException;

public class StopPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6604377277788735724L;

	@Override
	public void body() {
		try {
			getRobot().stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
