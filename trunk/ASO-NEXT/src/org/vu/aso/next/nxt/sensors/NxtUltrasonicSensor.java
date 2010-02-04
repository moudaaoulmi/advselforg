package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

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
