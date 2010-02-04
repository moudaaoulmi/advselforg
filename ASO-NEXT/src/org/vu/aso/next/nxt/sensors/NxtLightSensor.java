package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class NxtLightSensor implements INxtSensor {
	LightSensor sensor;
	
	public NxtLightSensor(SensorPort port) {
		sensor = new LightSensor(port);
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
}
