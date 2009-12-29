package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.ESensorType;

public class Sensor {
	
	private ESensorType sensorType;
	private int sensorValue;
	
	public Sensor(ESensorType s){
		
		this.sensorType = s;
	}
	
	public ESensorType getSensorType(){
		return this.sensorType;
	}
	
	public void setSensorValue(int v){
		this.sensorValue = v;
	}
	
	public int getSensorValue(){
		return this.sensorValue;
	}
	
	
	
}
