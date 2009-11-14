package org.vu.advselforg;

public interface RobotController {

	public void moveForward(int distance, boolean immediateReturn);
	public void moveBackward(int distance, boolean immediateReturn);
	public void turnLeft(int degrees, boolean immediateReturn);
	public void turnRight(int degrees, boolean immediateReturn);
	
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
