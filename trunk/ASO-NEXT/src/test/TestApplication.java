package test;

import java.io.PrintStream;

import lejos.nxt.MotorPort;

import org.vu.advselforg.common.EDistanceMode;
import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.NxtController;
import org.vu.advselforg.robotcontroller.RobotController;

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
				Thread.sleep(2000);
				out.println("");
				//out.printf("Distance onder: %d\n", r.getDistance(0, DistanceMode.LOWEST));
				//Thread.sleep(200);
				//out.printf("Distance boven: %d\n", r.getDistance(1, DistanceMode.HIGHEST_NOT255));
				out.println(" ");
				out.println("LightSensorValue " + r.getLightValue());
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
		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		
		ESensorType port1 = ESensorType.LIGHT; //onderste
		ESensorType port2 = ESensorType.NONE; //bovenste
		ESensorType port3 = ESensorType.NONE;
		ESensorType port4 = ESensorType.NONE;

		String[] names = {"ROSS"};

		for (int i = 0; i < names.length; i++) {
			robots[i] = new NxtController(names[i], leftMotorPort, rightMotorPort,
					motorReverse, port1, port2, port3, port4);
		}
	}

}
