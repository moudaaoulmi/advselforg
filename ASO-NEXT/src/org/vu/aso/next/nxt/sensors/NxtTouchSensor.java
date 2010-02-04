package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class NxtTouchSensor implements INxtSensor {
	TouchSensor sensor;
	
	public NxtTouchSensor(SensorPort port) {
		sensor = new TouchSensor(port);
	}
	
	public String getValue() {
		return sensor.isPressed() ? "1" : "0";
	}
	
}
