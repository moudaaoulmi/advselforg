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
		SensorType port1 = SensorType.TOUCH;
		SensorType port2 = SensorType.NONE;
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.NONE;

		robots[0] = new NxtController("ROSS", leftMotorPort, rightMotorPort,
				motorReverse, port1, port2, port3, port4);

		for (int i = 0; i < robots.length; i++) {
			//robots[i].moveForward(10, false);
			//robots[i].moveBackward(10, false);
			System.out.printf("Distance: %s\n", robots[i].getDistance());
			System.out.printf("LightValue: %s\n", robots[i].getLightValue());
			System.out.printf("Sound: %s\n", robots[i].getSoundLevel());
			System.out.printf("Touch: %s\n", new Boolean(robots[i].getTouchSensorPressed()).toString());
		}
	}
}
