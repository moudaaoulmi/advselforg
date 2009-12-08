import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import lejos.pc.comm.NXTConnector;

public class PCBrains {

	NXTConnector conn;
	InputStream in;
	OutputStream out;
	
	DataInputStream din;
	
	InputStreamReader reader;
	BufferedReader buff;

	PCBrains() throws Exception {
		conn = new NXTConnector();
		conn.connectTo("btspp://ROSS");
		
		in = conn.getInputStream();
		out = conn.getOutputStream();
		
		reader = new InputStreamReader(in);
		
		Thread.sleep(500);
	}
	
	private void run() throws Exception {
		
		ByteBuffer chars = ByteBuffer.allocate(1000);
		String message;
		
		for (int i = 0; i < 200; i++) {
			message = "2;";
			out.write(message.getBytes());
			out.flush();

			byte x;
			while((x = (byte) in.read()) != -1){
				chars.put(x);
			}
			chars.clear();
		}

		stop();
	}
	
	private void stop() throws Exception {
		out.write(new String("0;").getBytes());
		out.flush();
		conn.close();
	}

	public static void main(String[] args) throws Exception {
		new PCBrains().run();
	}
	
}
