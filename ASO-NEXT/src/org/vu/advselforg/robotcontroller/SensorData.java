package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.EMotorPort;
import org.vu.advselforg.common.EMovingMode;

public class SensorData {
	
	private int _distanceUpperSonar;
	private int _distanceLowerSonar;
	private boolean _touchSensorPressed;
	private int _motorATachoCount;
	private int _motorBTachoCount;
	private int _motorCTachoCount;
	private boolean _atDesiredMotorSpeed;
	private boolean _motorAIsScanning;
	private boolean _motorBIsScanning;
	private boolean _motorCIsScanning;
	private boolean _isTurningOrDrivingBack;
	private EMovingMode _lastCommand;
	private int _travelDistance;

	public void setLastCommand(EMovingMode mm){
		
		_lastCommand = mm;
	}
	
	
	public int getDistanceUpperSonar(){
		return _distanceUpperSonar;
		
	}
	public int getDistanceLowerSonar(){
		return _distanceLowerSonar;
		
	}

	public boolean getTouchSensorPressed(){
		return _touchSensorPressed;
		
	}

	public int getTachoMeterCount(EMotorPort port){
		
		if(port == EMotorPort.A){
			return _motorATachoCount; 
		}else if(port == EMotorPort.B){
			return _motorBTachoCount;
		}else{
			return _motorCTachoCount;
		}		
	}
	
	public boolean atDesiredMotorSpeed(){
		return true; //_atDesiredMotorSpeed;
	}
	
	public boolean isScanning(EMotorPort port){

		if(port == EMotorPort.A){
			return _motorAIsScanning; 
		}else if(port == EMotorPort.B){
			return _motorBIsScanning;
		}else{
			return _motorCIsScanning;
		}
	}
	
	public boolean isTurningOrDrivingBack(){
		return _isTurningOrDrivingBack;
		
	}
	
	public EMovingMode lastCommand(){
		return _lastCommand;
		
	}
	public int getTravelDistance(){
		return _travelDistance;
		
	}
	//At this moment I expect fixed sensortypes. Port 1 and 2 ultra sonic, port 3 licht, port 4 touch.
	//I think this one need some refactoring after I wrote it :)
	public void processMessage(String[] message){
		
		_distanceLowerSonar = Integer.parseInt(message[0]);
		_distanceUpperSonar = Integer.parseInt(message[1]);
		
		//Something for the light sensor.
		//message[2]
		_touchSensorPressed = message[3].equals("1") ? true : false; 
		
		_motorATachoCount = Integer.parseInt(message[4]);
		_motorBTachoCount = Integer.parseInt(message[5]);
		_motorCTachoCount = Integer.parseInt(message[6]);

		_travelDistance = Integer.parseInt(message[7]);
		
		_isTurningOrDrivingBack = message[8].equals("1") ? true : false;
		
		
		_motorAIsScanning = message[9].equals("1") ? true : false;
		_motorBIsScanning = message[9].equals("1") ? true : false;;
		_motorCIsScanning = message[9].equals("1") ? true : false;;
		
	}
}
