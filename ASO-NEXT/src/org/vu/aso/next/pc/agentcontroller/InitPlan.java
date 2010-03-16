package org.vu.aso.next.pc.agentcontroller;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.pc.NxtBridge;

public class InitPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 2961883448970106007L;

	String robotName;

	public InitPlan() {
		try {
			this.robotName = getRobotName();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		setBelief(Beliefs.ROBOT_NAME, this.robotName);
	}

	public String getRobotName() throws FileNotFoundException {
		// Get the first name
		Scanner in = new Scanner("C:\\names.txt");
		String name = in.nextLine();
		String rest = "";
		while (in.hasNext()) {
			rest += '\n' + in.nextLine();
		}
		in.close();

		// Print the remainder back to the file
		PrintStream out = new PrintStream("C:\\names.txt");
		out.print(rest);
		out.close();

		return name;
	}

	@Override
	public void body() {
		printDebug("executed initPlan");
		NxtBridge robot;

		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		ESensorType port1 = ESensorType.NONE;
		ESensorType port2 = ESensorType.ULTRASONIC;
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		float wheelDiameter = 5.4f;
		float trackWidth = 15.1f;

		robot = new NxtBridge(robotName, port1, false, port2, true, port3, true, port4, true, leftMotorPort,
				rightMotorPort, motorReverse, wheelDiameter, trackWidth);

		setBelief(Beliefs.ROBOT, robot);
		setBelief(Beliefs.INITIALIZED, true);

		printDebug("is connected");
	}
}
