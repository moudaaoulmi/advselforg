package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class NxtSoundSensor implements INxtSensor {
	SoundSensor sensor;
	
	public NxtSoundSensor(SensorPort port) {
		sensor = new SoundSensor(port);
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
}
