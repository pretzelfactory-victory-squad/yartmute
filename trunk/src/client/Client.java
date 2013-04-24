package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import common.toserver.Open;

public class Client {
	private Socket socket = null;
	private ClientDoc doc = null;
	private ClientSocketReader reader = null;
	private ClientSocketWriter writer = null;

	public Client(){
		
	}

	public boolean connect(String host, int port){
		try {
			socket = new Socket(host, port);
			System.out.println("Connected to "+host+":"+port);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String openFile(String file){
		writer.openFile(file);
		String contents = reader.waitForFile();
		return contents;
	}

	public void uploadFile(){

	}
	
	public String[] getFileList(){
		writer.getFileList();
		String[] list = reader.waitForFileList();
		return list;
	}

	public void closeConnection() {
		try{
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void insertCharacter(char c) {
		doc.insertCharacter(c);
	}
}
