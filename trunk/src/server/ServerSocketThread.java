package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class ServerSocketThread implements Runnable {
	private Socket s = null;
	OutputStreamWriter writer = null;
	InputStreamReader reader = null;
	
	public ServerSocketThread(Socket s) {
		this.s = s;
		try {
			reader = new InputStreamReader(s.getInputStream(), "UTF-8");
			writer = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
	}
	
	private void waitForCommand(){
		while(true){
			char[] cmd = new char[5];
				try {
					reader.read(cmd, 0, 5);
				} catch (IOException e) {
					e.printStackTrace();
				}
			String command = cmd.toString();
			switch(command){
			case "OPEN": open();
				break;
			case "LIST": list();
				break;
			case "CLOSE": close();
				break;
			case "WRITE": write();
				break;
			default: invalid();
		
			}
		}
	}
	
	private void invalid() {
		try{
		writer.write("Din mamma jobbar inte här, städa upp ditt kommando!");
		writer.flush();
		} catch (Exception e){}
	}
	private ServerDoc open(){
		
		return null;	
	}
	private List<ServerDoc> list(){
		return null;
		
	}
	private void close(){
		
	}
	private void write(){
		
	}
}
