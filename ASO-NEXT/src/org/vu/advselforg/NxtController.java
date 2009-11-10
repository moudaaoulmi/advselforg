package org.vu.advselforg;

import java.io.IOException;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.*;

public class NxtController implements RobotController {
	
	private NXTCommand nxtCommand;
	private NXTConnector conn;
	private NXTInfo nxt;
	private NXTComm nxtComm;

	public NxtController(String robotName) {
		conn = new NXTConnector();
		nxt = search(robotName);
		
		try {
			nxtComm = NXTCommFactory.createNXTComm(nxt.protocol);
			nxtComm.open(nxt);
			System.out.println("Connected to " + nxt.name);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		
		nxtCommand = new NXTCommand();
		nxtCommand.setNXTComm(nxtComm);
	}
	
	/* Close the connection */
	protected void finalize() throws Throwable {
		try {
			if (nxtCommand == null) {
				return;
			} else {
				nxtCommand.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			super.finalize();
		}
	}
	
	/* Find NXT's with a given name */
	private NXTInfo search(String robotName) {
		NXTInfo[] nxts;
		nxts = conn.search(robotName, null, NXTCommFactory.BLUETOOTH);
		if (nxts.length > 0) {
			return nxts[0];
		} else {
			return null;
		}
	}
	
	/* Move the robot [distance] cm forward */
	public void moveForward(int distance) {
		try {
			nxtCommand.setOutputState(0, (byte) 50, 0, 0, 0, 0, distance * 21);
			nxtCommand.setOutputState(2, (byte) 50, 0, 0, 0, 0, distance * 21);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
