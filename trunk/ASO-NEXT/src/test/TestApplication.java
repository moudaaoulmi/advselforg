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
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		int ultrasonicSensorPort = 4;
		int lightSensorPort = 1;

		robots[0] = new NxtController("ROSS", leftMotorPort, rightMotorPort,
				motorReverse, ultrasonicSensorPort, lightSensorPort);

		for (int i = 0; i < robots.length; i++) {
			robots[i].moveForward(50, true);
			System.out.println(robots[i].getDistance());
		}
	}
}
