package test;

import java.io.PrintStream;

import org.vu.advselforg.*;

public class TestApplication {

	private RobotController[] robots;
	private PrintStream out;

	public static void main(String[] args) {
		TestApplication app = new TestApplication();
		app.run();
	}

	public TestApplication() {
		robots = new RobotController[6];
		out = System.out;
	}

	private void run() {
		connectBots();

		for (int j = 0; j < 4; j++) {
			robots[0].moveForward(32, true);
			robots[1].moveForward(32, false);
			robots[0].turnLeft(90, true);
			robots[1].turnLeft(90, false);
		}
		
		//out.printf("Distance: %s\n", r.getDistance());
		//out.printf("LightValue: %s\n", r.getLightValue());
		//out.printf("Sound: %s\n", r.getSoundLevel());
		//out.printf("Touch: %s\n", new Boolean(r.getTouchSensorPressed()).toString());

	}

	private void connectBots() {
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		SensorType port1 = SensorType.NONE;
		SensorType port2 = SensorType.NONE;
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.NONE;

		String[] names = {"JOEY","CHANDLER"};

		for (int i = 0; i < names.length; i++) {
			robots[i] = new NxtController(names[i], leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);
		}
	}

}
