package org.vu.advselforg.common;

public class NxtProtocol {

	// Commands
	public final static int EXIT = 0;
	public final static int INIT = 1;
	public final static int SENSOR_DATA = 2;
	public final static int STOP = 3;
	public final static int FORWARD = 4;
	public final static int BACKWARD = 5;
	public final static int TURN_LEFT = 6;
	public final static int TURN_RIGHT = 7;
	public final static int RESET_TRAVEL_DISTANCE = 8;
	public final static int PERFORM_SCAN = 9;
	
	// Sensors
	public final static int NO_SENSOR = 0;
	public final static int ULTRASONIC_SENSOR = 1;
	public final static int LIGHT_SENSOR = 2;
	public final static int SOUND_SENSOR = 3;
	public final static int TOUCH_SENSOR = 4;
	public final static int RFID_SENSOR = 5;
	
	// OTHER
	public final static int TERMINATION_BYTE = -1;
	public final static int MESSAGE_SIZE = 50;
	
}
