package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class NxtLightSensor implements INxtSensor {
	LightSensor sensor;
	boolean toBeMonitored;
	
	public NxtLightSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new LightSensor(port, true);
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
}
