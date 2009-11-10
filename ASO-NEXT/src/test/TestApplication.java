package test;

import org.vu.advselforg.*;

public class TestApplication {
	
	private RobotController[] robots;

	public static void main(String[] args) {
		try {
			TestApplication instance = new TestApplication();
			instance.run();
		} catch (Throwable t) {
			System.err.println("Error: " + t.getMessage());
		}
	}

	public TestApplication() {
		robots = new RobotController[1];
	}

	private void run() {
		robots[0] = new NxtController("CHANDLER");
		robots[0].moveForward(50);
	}
}
