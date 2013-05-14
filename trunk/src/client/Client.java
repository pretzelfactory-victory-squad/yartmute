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

	/** 
	 * Connect to a server at {@code host}:{@code port}.
	 * @param host host address
	 * @param port port-number
	 * */
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
	
	/** 
	 * Open file with name {@code file} from the server. 
	 * The calling thread is paused until response is received
	 * @param file name of file
	 * */
	public ClientDoc openFile(String file){
		writer.openFile(file);
		String contents = reader.waitForFile();
		doc = new ClientDoc(reader, contents);
		return doc;
	}

	public void uploadFile(){

	}
	
	/** 
	 * Fetch the list of editable files from the server. 
	 * The calling thread is paused until response is received
	 * */
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

	/** 
	 * Send a write command to the server
	 * */
	public void queueUpdate(int lineStart, int lineEnd, int slotStart, int slotEnd, String insertion) {
		try{
			Write w = new Write(lineStart, lineEnd, slotStart, slotEnd, insertion, doc.getVersion());
			writer.sendCommand(w);
		}catch(CommandArgumentException e){
			e.printStackTrace();
		}
	}
	
	/** 
	 * Get the reader that reads commands from the 
	 * input stream.
	 * */
	public ClientSocketReader getReader() {
		return reader;
	}
	
	public static void main(String[] args) {
		boolean dummyLogin = false;
		if(args.length == 1){
			dummyLogin = Boolean.parseBoolean(args[0]);
		}
		Client client = new Client();
		new ClientGUI(client, dummyLogin);
	}
}
