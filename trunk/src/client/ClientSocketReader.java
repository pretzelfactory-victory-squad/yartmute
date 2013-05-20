package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import common.Command;
import common.CommandFactory;
import common.Log;
import common.toclient.SendFile;
import common.toclient.SendFileList;
import common.toserver.GetFileList;

/**
 * Reads the socket input stream and saves incoming commands to a list.
 */
public class ClientSocketReader extends Observable{

	private boolean isConnected;
	private BufferedReader reader;
	private Thread thread;
	private List<Command> commands = new ArrayList<Command>();

	/**
	 * Reads the socket input stream and saves incoming commands to a list.
	 * @param client The client object
	 * @param socket The socket object
	 */
	public ClientSocketReader(final Client client, Socket socket){
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		} catch (UnknownHostException e) {
			Log.error(e);
			return;
		} catch (IOException e) {
			Log.error(e);
			return;
		}

		isConnected = true;
		thread = new Thread(new Runnable(){
			public void run(){
				try {
					while(isConnected){
						waitForUpdate();
					}
				} catch (SocketException e) {
					if(e.getMessage().equals("Connection reset")){
						client.socketReset(e);
					}else{
						Log.error(e);
					}
				} catch (IOException e) {
					Log.error(e);
				} finally {
					client.closeConnection();
				}
			}
		}, "ClientSocketReader");
		thread.start();
	}

	/**
	 * Wait for a new line in the input stream and creates a command from it.
	 * @throws IOException
	 */
	public void waitForUpdate() throws IOException{
		String line = reader.readLine();
		if(line == null){
			isConnected = false;
			return;
		}
		Log.debug("Cmd received: "+line);
		Command c = CommandFactory.getCommand(line);
		commands.add(c);
		setChanged();
		notifyObservers();
		synchronized(this){
			notifyAll();
		}
	}

	/**
	 * Pauses the calling thread until a command of type {@code type} is received
	 * @param type The type of the command
	 * @return
	 */
	Command waitForCommand(String type) {
		try {
			boolean wait = true;
			while(wait){
				for(Command c : commands){
					if(c.getType().equals(type)){
						wait = false;
						commands.remove(c);
						return c;
					}
				}
				synchronized(this){
					Log.debug("Waiting for:  "+type+" response (Thread:"+Thread.currentThread().getName()+")");
					wait();
				}
			}
			return null;
		} catch (InterruptedException e) {
			Log.error(e);
			return null;
		}
	}

	/**
	 * Pauses the calling thread until a {@code SendFileList}-command is received
	 * @return The {@code SendFileList} command
	 */
	public String[] waitForFileList(){
		SendFileList cmd = (SendFileList)waitForCommand(SendFileList.TYPE);
		return cmd.getFileList();
	}

	/**
	 * Pauses the calling thread until a {@code SendFile}-command is received
	 * @return The file
	 */
	public String waitForFile() {
		SendFile cmd = (SendFile)waitForCommand(SendFile.TYPE);
		return cmd.getFile();
	}

	/**
	 * Search the list of received commands for one of the specified type
	 * @return A command of the specified type or null 
	 */
	public Command getCommand(String type) {
		for(Command c: commands){
			if(c.getType().equals(type)){
				commands.remove(c);
				return c;
			}
		}
		return null;
	}
}
