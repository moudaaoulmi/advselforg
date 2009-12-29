package org.vu.advselforg.robotcontroller;

import org.vu.advselforg.common.EDistanceMode;
import org.vu.advselforg.common.EMovingMode;
import org.vu.advselforg.common.EMotorPort;

public interface RobotController {

	// Motor actions
	public void moveForward(float distance, boolean immediateReturn);
	public void moveBackward(float distance, boolean immediateReturn);
	public void turnLeft(float angle, boolean immediateReturn);
	public void turnRight(float angle, boolean immediateReturn);
	public void stop();
	
	// Sensor actions
	public int getDistance(int sensorIndex, EDistanceMode mode);
	public int getDistance(EDistanceMode mode);
	public int getLightValue(int sensorIndex);
	public int getLightValue();
	public int getSoundLevel(int sensorIndex);
	public int getSoundLevel();
	public boolean getTouchSensorPressed(int sensorIndex);
	public boolean getTouchSensorPressed();
	public long getRFID(int sensorIndex);
	public long getRFID();
	
	//TODO implement plox :) Method namen mogen anders....
	public int getTachoMeterCount(EMotorPort port);
	public boolean atDesiredMotorSpeed();
	public void performScan(EMotorPort port, int fromAngle, int toAngle, int speed);
	//Start op from angle (zal dus negatief zijn) en gaat tot to Angle. Als hij klaar is 
	// dan zet hij de tachometer weer op positie 0. (Check dit als je het gedaan hebt ivm
	// coast problemen).
	public boolean isScanning(EMotorPort port);
	public boolean isTurningOrDrivingBack();
	public EMovingMode lastCommand();
	public int getTravelDistance();
	public void resetTravelDistance();
	public void calibrateTurret(EMotorPort port);
	// Deze geeft een int terug en staat op 0 nadat hij een andere actie heeft uitgevoerd dan 
	// draaien. Ik wil dus eigenlijk alleen kunnen afleiden hoeveel hij naar voren heeft
	// gereden.
}
