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

		RobotController r = robots[0];
		while (true) {
			try {
				Thread.sleep(1000);
				out.println("");
				out.printf("Distance onder: %d\n", r.getDistance(0, DistanceMode.LOWEST));
				Thread.sleep(200);
				out.printf("Distance boven: %d\n", r.getDistance(1, DistanceMode.HIGHEST_NOT255));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*r.moveForward(200, true);
		while (true) {
			if (r.getTouchSensorPressed()) {
				r.stop();
				break;
			}
		}*/
		
		/*for (int j = 0; j < 4; j++) {
			robots[0].moveForward(32, true);
			robots[1].moveForward(32, false);
			robots[0].turnLeft(90, true);
			robots[1].turnLeft(90, false);
		}*/
		
		//out.printf("Distance: %s\n", robots[0].getDistance());
		//out.printf("LightValue: %s\n", r.getLightValue());
		//out.printf("Sound: %s\n", r.getSoundLevel());
		//out.printf("Touch: %s\n", new Boolean(r.getTouchSensorPressed()).toString());

	}

	private void connectBots() {
		int leftMotorPort = 1;
		int rightMotorPort = 3;
		boolean motorReverse = false;
		
		SensorType port1 = SensorType.ULTRASONIC; //onderste
		SensorType port2 = SensorType.ULTRASONIC; //bovenste
		SensorType port3 = SensorType.NONE;
		SensorType port4 = SensorType.TOUCH;

		String[] names = {"JOEY"};

		for (int i = 0; i < names.length; i++) {
			robots[i] = new NxtController(names[i], leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);
		}
	}

}
