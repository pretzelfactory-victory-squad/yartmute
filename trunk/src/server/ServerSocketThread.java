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
	ServerDoc doc = null;

	public ServerSocketThread(Socket s) {
		this.s = s;
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {

	}

	private void waitForCommand(){
		while(true){
			Command command = cmdFactory.getCommand(reader.readLine());
		}
	}

	private void invalid() {
		try{
			writer.write("Din mamma jobbar inte här, städa upp ditt kommando!");
			writer.flush();
		} catch (Exception e){}
	}
	private void open(){
		doc = ServerDoc.getDoc(filename)
	}
	private void list(){


	}
	private void close(){

	}
	private void write(){

	}

	private String readStuff(){
		char[] temp = new char[];
		try {
			reader.read(temp, 0, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = temp.toString(); 
		return s;
	}
}
