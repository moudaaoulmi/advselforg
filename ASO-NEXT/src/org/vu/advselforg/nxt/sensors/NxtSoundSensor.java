package org.vu.advselforg.nxt.sensors;

import org.vu.advselforg.nxt.INxtSensor;

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
