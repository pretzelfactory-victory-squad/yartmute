package client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import common.Command.CommandArgumentException;
import common.Log;
import common.toclient.SendFile;
import common.toserver.Open;
import common.toserver.Write;

public class Client extends Observable{
	private Socket socket = null;
	private ClientDoc doc = null;
	private ClientSocketReader reader = null;
	private ClientSocketWriter writer = null;
	private boolean connected = false;
	private int userId;
	
	public Client(){
		
	}

	/** 
	 * Connect to a server at {@code host}:{@code port}.
	 * @param host host address
	 * @param port port-number
	 * */
	public boolean connect(String host, int port) throws ServerException{
		try {
			socket = new Socket(host, port);
			reader = new ClientSocketReader(this, socket);
			writer = new ClientSocketWriter(socket.getOutputStream());
			connected = true;
			Log.message("Connected to "+host+":"+port);
			return true;
		} catch (ConnectException e) {
			throw new ServerException(e.getMessage());
		} catch (UnknownHostException e) {
			throw new ServerException(e.getMessage());
		} catch (IOException e) {
			Log.error(e);
			return false;
		}
	}
	
	/** 
	 * Open file with name {@code file} from the server. 
	 * The calling thread is paused until response is received
	 * @param file name of file
	 * @throws ServerException
	 * @throws SocketException 
	 * */
	public ClientDoc openFile(String file) throws ServerException{
		try {
			writer.openFile(file);
		} catch (SocketException e) {
			throw new ServerException(e.getMessage());
		}
		SendFile cmd = (SendFile) reader.waitForCommand(SendFile.TYPE);
		if(cmd.isSuccessful()){
			userId = cmd.getUserId();
			doc = new ClientDoc(this, cmd.getFile(), cmd.getVersion());
			return doc;
		}else{
			throw new ServerException(cmd.getErrorMsg());
		}
	}

	public void uploadFile(){

	}
	
	/** 
	 * Fetch the list of editable files from the server. 
	 * The calling thread is paused until response is received
	 * @throws SocketException 
	 * */
	public String[] getFileList() throws ServerException{
		try {
			writer.getFileList();
		} catch (SocketException e) {
			throw new ServerException(e.getMessage());
		}
		String[] list = reader.waitForFileList();
		return list;
	}
	
	public void socketReset(Exception e){
		if(connected){
			connected = false;
			setChanged();
			notifyObservers(e);
		}
	}

	public void closeConnection() {
		connected = false;
		try{
			socket.close();
		}catch(IOException e){
			Log.error(e);
		}
	}

	/** 
	 * Send a write command to the server
	 * */
	public void queueUpdate(int lineStart, int lineEnd, int slotStart, int slotEnd, String insertion) throws ServerException{
		try{
			Write w = new Write(lineStart, lineEnd, slotStart, slotEnd, insertion, userId, doc.getVersion());
			writer.sendCommand(w);
		}catch(CommandArgumentException e){
			Log.error(e);
		} catch (SocketException e) {
			socketReset(e);
		}
	}
	
	/** 
	 * Get the reader that reads commands from the 
	 * input stream.
	 * */
	public ClientSocketReader getReader() {
		return reader;
	}
	
	/** 
	 * Get the user id
	 * @return user id (integer)
	 * */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Called when observers been notified of change
	 * @return true if connected to a server
	 */
	public boolean isConnected() {
		return connected;
	}
	
	public static class ServerException extends IOException{
		public ServerException(String errorMsg) {
			super(errorMsg);
		}
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
