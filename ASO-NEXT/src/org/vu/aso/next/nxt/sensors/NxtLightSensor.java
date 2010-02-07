package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.robotics.Colors.Color;

public class NxtLightSensor implements INxtSensor {
	LightSensor sensor;
	boolean toBeMonitored;
	
	public NxtLightSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new LightSensor(port);
		sensor.setFloodlight(true);
		sensor.setFloodlight(Color.BLUE);
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
}
