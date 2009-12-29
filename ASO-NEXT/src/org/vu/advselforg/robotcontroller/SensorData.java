package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.EMovingMode;

public class SensorData {

	public int getDistanceUpperSonar(){
		return 0;
		
	}
	public int getDistanceLowerSonar(){
		return 0;
		
	}

	public boolean getTouchSensorPressed(){
		return false;
		
	}

	public int getTachoMeterCount(EMotorPort port){
		return 0;
		
	}
	public boolean atDesiredMotorSpeed(){
		return false;
	}
	
	public boolean isScanning(EMotorPort port){
		return true;
	}
	
	public boolean isTurningOrDrivingBack(){
		return false;
		
	}
	
	public EMovingMode lastCommand(){
		return null;
		
	}
	public int getTravelDistance(){
		return 0;
		
	}
	
	public void processMessage(String message){
		
	}
}
