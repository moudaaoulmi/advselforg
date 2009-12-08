import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import lejos.pc.comm.NXTConnector;

public class PCBrains {

	NXTConnector conn;
	InputStream in;
	OutputStream out;
	
	InputStreamReader reader;
	BufferedReader buff;

	PCBrains() throws Exception {
		conn = new NXTConnector();
		conn.connectTo("btspp://ROSS");
		
		in = conn.getInputStream();
		out = conn.getOutputStream();
		
		reader = new InputStreamReader(in);
		buff = new BufferedReader(reader);
		
		Thread.sleep(500);
	}
	
	private void run() throws Exception {
		
		char[] chars = new char[30];
		String message;
		
		for (int i = 0; i < 200; i++) {
			message = "2;";
			out.write(message.getBytes());
			out.flush();
			System.out.println("Message " + i + " sent");
			buff.
			System.out.println(new String(chars));
		}

		stop();
	}
	
	private void stop() throws Exception{
		out.write(new String("0;").getBytes());
		out.flush();
		conn.close();
	}

	public static void main(String[] args) throws Exception {
		new PCBrains().run();
	}
	
}
