package org.vu.aso.next.pc.agentcontroller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import org.vu.aso.next.common.EMotorPort;
import org.vu.aso.next.common.ESensorType;
import org.vu.aso.next.common.NxtSettings;
import org.vu.aso.next.pc.NxtBridge;

public class InitPlan extends BeliefUpdatingPlan {

	private static final long serialVersionUID = 2961883448970106007L;

	String robotName;

	public InitPlan() {
	}

	public String getRobotName() {
		// Get the first name
		Scanner in;
		try {
			in = new Scanner(new FileInputStream(NxtSettings.NAMES_PATH));
		} catch (FileNotFoundException e) {
			in = new Scanner(NxtSettings.ROBOT_NAMES);
		}
		String name = in.nextLine();
		String rest = "";
		while (in.hasNext()) {
			rest += in.nextLine() + '\n';
		}
		in.close();

		// Print the remainder back to the file
		PrintStream out;
		try {
			out = new PrintStream(NxtSettings.NAMES_PATH);
			out.print(rest);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return name;
	}

	@Override
	public void body() {
		waitFor(new Random().nextInt(200));
		this.robotName = getRobotName();
		setBelief(Beliefs.ROBOT_NAME, this.robotName);
		
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
