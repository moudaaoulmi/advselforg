package org.vu.advselforg.nxt;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.RFIDSensor;

public class NxtRFIDSensor implements Sensor {
	RFIDSensor sensor;
	
	NxtRFIDSensor(SensorPort port) {
		sensor = new RFIDSensor(port);
	}
	
	public String getValue() {
		return new Long(sensor.readTransponderAsLong(false)).toString();
	}
	
}
