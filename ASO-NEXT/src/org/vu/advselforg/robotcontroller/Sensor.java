package org.vu.advselforg.robotcontroller;

public class Sensor {
	
	private SensorType sensorType;
	private int sensorValue;
	
	public Sensor(SensorType s){
		
		this.sensorType = s;
	}
	
	public SensorType getSensorType(){
		return this.sensorType;
	}
	
	public void setSensorValue(int v){
		this.sensorValue = v;
	}
	
	public int getSensorValue(){
		return this.sensorValue;
	}
	
	
	
}
