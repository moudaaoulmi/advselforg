package org.vu.advselforg.robotcontroller;

public interface RobotController {

	// Motor actions
	public void moveForward(float distance, boolean immediateReturn);
	public void moveBackward(float distance, boolean immediateReturn);
	public void turnLeft(float angle, boolean immediateReturn);
	public void turnRight(float angle, boolean immediateReturn);
	public void stop();
	
	// Sensor actions
	public int getDistance(int sensorIndex, DistanceMode mode);
	public int getDistance(DistanceMode mode);
	public int getLightValue(int sensorIndex);
	public int getLightValue();
	public int getSoundLevel(int sensorIndex);
	public int getSoundLevel();
	public boolean getTouchSensorPressed(int sensorIndex);
	public boolean getTouchSensorPressed();
	public long getRFID(int sensorIndex);
	public long getRFID();
	
	//TODO implement plox :) Method namen mogen anders....
	public int getTachoMeterCount(int motorIndex);
	public boolean atDesiredMotorSpeed();
	public void performScan(int motorIndex, int fromAngle, int toAngle, int speed);
	//Start op from angle (zal dus negatief zijn) en gaat tot to Angle. Als hij klaar is 
	// dan zet hij de tachometer weer op positie 0. (Check dit als je het gedaan hebt ivm
	// coast problemen).
	public boolean isScanning(int motorIndex);
	public boolean isTurning();
	public boolean isDrivingForward();
	public boolean isDrivingBackward();
	public int getTravelDistance();
	public void resetTravelDistance();
	// Deze geeft een int terug en staat op 0 nadat hij een andere actie heeft uitgevoerd dan 
	// draaien. Ik wil dus eigenlijk alleen kunnen afleiden hoeveel hij naar voren heeft
	// gereden.
}
