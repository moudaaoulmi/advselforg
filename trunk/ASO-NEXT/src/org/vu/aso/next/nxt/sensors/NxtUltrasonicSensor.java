package org.vu.aso.next.nxt.sensors;

import java.util.ArrayList;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class NxtUltrasonicSensor implements INxtSensor {

	static final int NUMBER_OF_MEASUREMENTS = 8;
	public UltrasonicSensor sensor;
	private boolean toBeMonitored;

	public NxtUltrasonicSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new UltrasonicSensor(port);
		this.toBeMonitored = toBeMonitored;
	}

	public String getValue() {
		return new Integer(sensor.getDistance()).toString();
	}

	public boolean toBeMonitored() {
		return toBeMonitored;
	}

	public void on() {
		sensor.continuous();
	}

	public void off() {
		sensor.off();
	}

}
