package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import common.Command;
import common.CommandFactory;

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
			try {
				Command command = CommandFactory.getCommand(reader.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void invalid() {
		try{
			writer.write("Din mamma jobbar inte här, städa upp ditt kommando!");
			writer.flush();
		} catch (Exception e){}
	}
	private void open(){
		doc = ServerDoc.getDoc("filename");
	}
	private void list(){


	}
	private void close(){

	}
	private void write(){

	}
}
