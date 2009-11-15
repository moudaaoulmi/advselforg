package test;

import java.io.PrintStream;

import org.vu.advselforg.*;

public class TestApplication {

	private RobotController[] robots;
	private PrintStream out;

	public static void main(String[] args) {
		try {
			TestApplication app = new TestApplication();
			app.run();
		} catch (Throwable t) {
			System.err.println("Error: " + t.getMessage());
		}
	}

	public TestApplication() {
		robots = new RobotController[1];
		out = System.out;
	}

	private void run() {
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		SensorType port1 = SensorType.TOUCH;
		SensorType port2 = SensorType.NONE;
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.NONE;

		String[] names = {"ROSS"};

		for (int i = 0; i < names.length; i++) {
			RobotController r = robots[i];
			
			r = new NxtController(names[i], leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);
			
			r.moveForward(10, false);
			//r.moveBackward(10, false);
			r.turnLeft(90, false);
			out.printf("Distance: %s\n", r.getDistance());
			out.printf("LightValue: %s\n", r.getLightValue());
			out.printf("Sound: %s\n", r.getSoundLevel());
			out.printf("Touch: %s\n", new Boolean(r.getTouchSensorPressed()).toString());
		}
	}
}
