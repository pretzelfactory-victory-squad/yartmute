package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import common.Command.CommandArgumentException;
import common.toserver.Open;
import common.toserver.Write;

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
			reader = new ClientSocketReader(this, socket);
			writer = new ClientSocketWriter(socket.getOutputStream());
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
	
	public ClientDoc openFile(String file){
		writer.openFile(file);
		String contents = reader.waitForFile();
		doc = new ClientDoc(reader, contents);
		return doc;
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

	public void queueUpdate(int lineStart, int lineEnd, int slotStart, int slotEnd, String insertion) {
		try{
			Write w = new Write(lineStart, lineEnd, slotStart, slotEnd, insertion, doc.getVersion());
			writer.sendCommand(w);
		}catch(CommandArgumentException e){
			e.printStackTrace();
		}
	}

	public ClientSocketReader getReader() {
		return reader;
	}
}
