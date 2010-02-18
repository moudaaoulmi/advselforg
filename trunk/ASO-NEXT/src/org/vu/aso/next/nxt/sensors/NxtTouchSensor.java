package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class NxtTouchSensor implements INxtSensor {
	TouchSensor sensor;
	boolean toBeMonitored;
	
	public NxtTouchSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new TouchSensor(port);
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		return sensor.isPressed() ? "1" : "0";
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
	public void on() {
	}

	public void off() {
	}
}
