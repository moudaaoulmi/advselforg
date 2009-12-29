package org.vu.advselforg.nxt.sensors;

import org.vu.advselforg.nxt.INxtSensor;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.RFIDSensor;

public class NxtRFIDSensor implements INxtSensor {
	RFIDSensor sensor;
	
	public NxtRFIDSensor(SensorPort port) {
		sensor = new RFIDSensor(port);
	}
	
	public String getValue() {
		return new Long(sensor.readTransponderAsLong(false)).toString();
	}
	
}
