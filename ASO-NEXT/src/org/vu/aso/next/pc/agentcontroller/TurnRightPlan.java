package org.vu.aso.next.pc.agentcontroller;

public class TurnRightPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = -6357593709536921293L;
	private int angle;

	public TurnRightPlan(int angle) {
		this.angle = angle;
	}

	@Override
	public void body() {
		printDebug("executed TurnRightPlan(" + angle + ")");
		getRobot().turnRight(angle);
	}
}
