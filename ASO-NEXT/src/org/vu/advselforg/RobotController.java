package org.vu.advselforg;

public interface RobotController {

	// Motor actions
	public void moveForward(float distance, boolean immediateReturn);
	public void moveBackward(float distance, boolean immediateReturn);
	public void turnLeft(float angle, boolean immediateReturn);
	public void turnRight(float angle, boolean immediateReturn);
	public void stop();
	
	// Sensor actions
	public int getDistance(int sensorIndex);
	public int getDistance();
	public int getLightValue(int sensorIndex);
	public int getLightValue();
	public int getSoundLevel(int sensorIndex);
	public int getSoundLevel();
	public boolean getTouchSensorPressed(int sensorIndex);
	public boolean getTouchSensorPressed();
	public long getRFID(int sensorIndex);
	public long getRFID();

}
