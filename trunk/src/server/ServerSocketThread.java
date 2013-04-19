package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
			String command = reader.read();
			
			switch(command){
			case "OPEN": open();
				
			case "LIST": list();
			
			case "CLOSE": close();
			
			case "WRITE": write();
			
		
			}
		}
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
