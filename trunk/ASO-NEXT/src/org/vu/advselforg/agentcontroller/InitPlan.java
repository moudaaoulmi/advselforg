package org.vu.advselforg.agentcontroller;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.ESensorType;
import org.vu.advselforg.robotcontroller.*;

import sun.awt.EmbeddedFrame;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.IFilter;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class InitPlan extends Plan{

	private static final long serialVersionUID = 2961883448970106007L;
	
	String agentName;
	
	public InitPlan(String agentName){
		this.agentName = agentName;
	}
	
	
	@Override
	public void body() {
			
		NxtBridge robot;

		
		EMotorPort leftMotorPort = EMotorPort.A;
		EMotorPort rightMotorPort = EMotorPort.C;
		boolean motorReverse = false;
		ESensorType port1 = ESensorType.ULTRASONIC;
		ESensorType port2 = ESensorType.ULTRASONIC;
		ESensorType port3 = ESensorType.LIGHT;
		ESensorType port4 = ESensorType.TOUCH;
		try {
			robot = new NxtBridge("JOEY", port1, port2, port3, 
					port4, leftMotorPort, rightMotorPort, motorReverse, 5.4f, 15.1f);
		
			
		getBeliefbase().getBelief("robot").setFact(robot);
		System.out.println("Connected");
			
		getBeliefbase().getBelief("Initialized").setFact(true);
		} catch (Exception e) {
			System.out.println("Whoops, does not compute. Cannot connect to robot.");
		}
	}	
}

