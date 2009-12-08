import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTConnector;

public class PCBrains {

	NXTConnector conn;
	InputStream in;
	OutputStream out;

	PCBrains() throws Exception {
		conn = new NXTConnector();
		conn.connectTo("btspp://ROSS");
		
		in = conn.getInputStream();
		out = conn.getOutputStream();
		
		Thread.sleep(500);
	}
	
	private void run() throws Exception {
		
		StringBuffer sb = new StringBuffer();
		String message;
		byte b;
		
		
		
		message = "1;0;0;0;0;1;3;5.4;15.1;0;";
		out.write(message.getBytes());
		out.flush();

		while((b = (byte) in.read()) != -1){
			sb.append((char) b);
		}
		System.out.println(sb.toString());
		sb.setLength(0);

		
		
		message = "4;20;";
		out.write(message.getBytes());
		out.flush();

		while((b = (byte) in.read()) != -1){
			sb.append((char) b);
		}
		System.out.println(sb.toString());
		sb.setLength(0);
		
		
		
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
