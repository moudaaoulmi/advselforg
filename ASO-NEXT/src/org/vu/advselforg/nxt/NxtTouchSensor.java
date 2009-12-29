package org.vu.advselforg.nxt;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class NxtTouchSensor implements Sensor {
	TouchSensor sensor;
	
	NxtTouchSensor(SensorPort port) {
		sensor = new TouchSensor(port);
	}
	
	public String getValue() {
		return sensor.isPressed() ? "1" : "0";
	}
	
}
