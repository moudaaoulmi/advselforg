import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class NxtLightSensor implements Sensor {
	LightSensor sensor;
	
	NxtLightSensor(SensorPort port) {
		sensor = new LightSensor(port);
	}
	
	public String getValue() {
		return new Integer(sensor.readValue()).toString();
	}
	
}
