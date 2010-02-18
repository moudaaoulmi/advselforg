package org.vu.aso.next.nxt.sensors;

import org.vu.aso.next.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.RFIDSensor;

public class NxtRFIDSensor implements INxtSensor {
	RFIDSensor sensor;
	boolean toBeMonitored;
	
	public NxtRFIDSensor(SensorPort port, boolean toBeMonitored) {
		sensor = new RFIDSensor(port);
		this.toBeMonitored = toBeMonitored;
	}
	
	public String getValue() {
		return new Long(sensor.readTransponderAsLong(false)).toString();
	}
	
	public boolean toBeMonitored() {
		return toBeMonitored;
	}
	
	public void on() {
	}

	public void off() {
	}
}
