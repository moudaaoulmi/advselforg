package org.vu.advselforg;

public interface RobotController {

	public void moveForward(int distance, boolean immediateReturn);
	public void moveBackward(int distance, boolean immediateReturn);
	public void turnLeft(int degrees, boolean immediateReturn);
	public void turnRight(int degrees, boolean immediateReturn);
	
	public int getDistance();
	public int getLightValue();
	public int getTouchSensorPressed();
	public int getRFID();

}
