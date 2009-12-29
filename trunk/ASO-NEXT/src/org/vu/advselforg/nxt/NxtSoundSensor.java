package org.vu.advselforg.nxt;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class NxtSoundSensor implements Sensor {
	SoundSensor sensor;
	
	NxtSoundSensor(SensorPort port) {
		sensor = new SoundSensor(port);
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
}
