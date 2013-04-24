package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
		return file;
	}

	public void uploadFile(){

	}
	
	public List<String> getFileList(){
		writer.getFileList();
		reader.waitForFileList();
		
		List<String> list = new ArrayList<String>();
		list.add("hoahoahoahoa.txt");
		list.add("qewqweqwe.txt");
		list.add("iopiopiop.txt");
		list.add("pewpewpewpew.txt");
		
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
