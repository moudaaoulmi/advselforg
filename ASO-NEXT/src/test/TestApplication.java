package test;

import java.io.IOException;

import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.NXTProtocol;
import lejos.pc.comm.*;
import lejos.pc.tools.*;



public class TestApplication implements NXTProtocol {
	private NXTCommand[] nxtCommands;
	private NXTCommand nxtCommand;
	private NXTConnector conn = new NXTConnector();
	private NXTInfo[] nxts;
	private NXTConnectionModel nm;
	private NXTComm[] nxtComms;
	private NXJControl control;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestApplication instance = new TestApplication();
			instance.run();
		} catch (Throwable t) {
			System.err.println("Error: " + t.getMessage());
		}

	}
	
	public TestApplication() {
		nxtComms = new NXTComm[4];
	}
	
	private void run(){
		
		//When window is closed, close everything.
		nxts = search();
		
		for(int i = 0; i < nxts.length; i++){
			NXTComm nxtComm;
			try {
				nxtComm = NXTCommFactory.createNXTComm(nxts[i].protocol);
				nxtComm.open(nxts[i]);
				
				nxtComms[i] = nxtComm;
				System.out.println("Connected to " + nxts[i].name);
				
				
			} catch (NXTCommException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closeAll();	
			}
	
		}
		
		for(int i = 0; i < nxtComms.length; i++){
		nxtCommand = new NXTCommand();
		nxtCommand.setNXTComm(nxtComms[i]);
		
		try {
			nxtCommand.setOutputState(0, (byte) 100, 0, 0, 0, 0, 5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		for(int i = 0; i < nxtComms.length; i++){
			nxtCommand = new NXTCommand();
			nxtCommand.setNXTComm(nxtComms[i]);
			
			try {
				nxtCommand.setOutputState(0, (byte) 100, 0, 0, 0, 0, 5000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i < nxtComms.length; i++){
			nxtCommand = new NXTCommand();
			nxtCommand.setNXTComm(nxtComms[i]);
			
			try {
				nxtCommand.setOutputState(0, (byte) 100, 0, 0, 0, 0, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		closeAll();	
	}
	
	
	private NXTInfo[] search() {
		closeAll();
		//Search for all bluetooth nxt devices.
		nxts = conn.search(null, null, getProtocols());
		return nxts;
	}
	
	private int getProtocols(){
	return NXTCommFactory.BLUETOOTH;	
	}
	

	
	//Close all connections to the NEXTS
	private void closeAll() {
		if (nxtCommands == null) return;
		for (int i = 0; i < nxtCommands.length; i++) {
			NXTCommand nc = nxtCommands[i];
			if (nc != null)
				try {
					nc.close();
				} catch (IOException ioe) {}
		}
		nxtCommand = null;
	}

}
