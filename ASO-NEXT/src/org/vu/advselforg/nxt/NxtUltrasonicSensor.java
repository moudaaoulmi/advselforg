package org.vu.advselforg.nxt;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class NxtUltrasonicSensor implements Sensor {
	UltrasonicSensor sensor;
	
	NxtUltrasonicSensor(SensorPort port) {
		sensor = new UltrasonicSensor(port);
		sensor.off();
	}
	
	public String getValue() {
		sensor.ping();
		return new Integer(sensor.getDistance()).toString();
	}
	
}
