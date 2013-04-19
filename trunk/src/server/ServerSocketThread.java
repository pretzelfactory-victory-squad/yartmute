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
	BufferedWriter writer = null;
	BufferedReader reader = null;
	
	public ServerSocketThread(Socket s) {
		this.s = s;
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
	}
	
	private void waitForCommand(){
		while(true){
			String command = reader.;
			
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
