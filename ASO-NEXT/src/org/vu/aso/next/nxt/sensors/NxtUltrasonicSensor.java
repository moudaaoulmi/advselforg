package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class NxtUltrasonicSensor implements INxtSensor {
	UltrasonicSensor sensor;
	boolean toBeMonitored;
	
	public NxtUltrasonicSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new UltrasonicSensor(port);
		sensor.off();
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		sensor.ping();
		return new Integer(sensor.getDistance()).toString();
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
}
