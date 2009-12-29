package org.vu.advselforg.nxt.sensors;

import org.vu.advselforg.nxt.INxtSensor;

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
