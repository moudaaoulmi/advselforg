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

	public void ping() {
		sensor.ping();
	}

	public int getDistance() {
		int[] distances = new int[NUMBER_OF_MEASUREMENTS];
		sensor.getDistances(distances);
		ArrayList list = new ArrayList(NUMBER_OF_MEASUREMENTS);
		list.add(distances[0]);

		for (int i = 1; i < NUMBER_OF_MEASUREMENTS; i++) {
			int j = 0;
			while (j < 1 && distances[i] > (Integer) list.get(j)) {
				j++;
			}
			list.add(j, distances[i]);
		}

		//return ((Integer) list.get(3) + (Integer) list.get(4)) / 2;
		LCD.drawString("dist: " + list.get(0), 0, 1);
		Button.waitForPress();
		return (Integer) list.get(0);
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
