package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class NxtSoundSensor implements INxtSensor {
	SoundSensor sensor;
	boolean toBeMonitored;
	
	public NxtSoundSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new SoundSensor(port);
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
}
