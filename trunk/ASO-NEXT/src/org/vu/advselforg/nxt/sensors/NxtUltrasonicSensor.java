package org.vu.advselforg.nxt.sensors;

import org.vu.advselforg.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class NxtUltrasonicSensor implements INxtSensor {
	UltrasonicSensor sensor;
	
	public NxtUltrasonicSensor(SensorPort port) {
		sensor = new UltrasonicSensor(port);
		sensor.off();
	}
	
	public String getValue() {
		sensor.ping();
		return new Integer(sensor.getDistance()).toString();
	}
	
}
