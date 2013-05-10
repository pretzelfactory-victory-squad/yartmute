package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import common.Command;
import common.CommandFactory;
import common.toclient.SendFile;
import common.toclient.SendFileList;
import common.toserver.GetFileList;

public class ClientSocketReader extends Observable{

	private boolean isConnected;
	private BufferedReader reader;
	private Thread thread;
	private List<Command> commands = new ArrayList<Command>();

	public ClientSocketReader(final Client client, Socket socket){
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		isConnected = true;
		thread = new Thread(new Runnable(){
			public void run(){
				try {
					while(isConnected){
						waitForUpdate();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					client.closeConnection();
				}
			}
		}, "ClientSocketReader");
		thread.start();
	}

	public void waitForUpdate() throws IOException{
		System.out.println("waiting for server response");
		String line = reader.readLine();
		System.out.println("line received"+line);
		if(line == null){
			isConnected = false;
			return;
		}

		try {
			System.out.println("Command received: "+line);
			Command c = CommandFactory.getCommand(line);
			commands.add(c);
			setChanged();
			notifyObservers();
			synchronized(this){
				notifyAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Command waitForCommand(String type) {
		try {
			boolean wait = true;
			while(wait){
				for(Command c : commands){
					System.out.println("type:"+c.getType()+" = "+type);
					if(c.getType().equals(type)){
						wait = false;
						commands.remove(c);
						return c;
					}
				}
				System.out.println("Waiting for "+type+" command");
				synchronized(this){
					System.out.println(Thread.currentThread().getName()+": Waiting for "+type+" response");
					wait();
				}
			}
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String[] waitForFileList(){
		SendFileList cmd = (SendFileList)waitForCommand(SendFileList.TYPE);
		return cmd.getFileList();
	}

	public String waitForFile() {
		SendFile cmd = (SendFile)waitForCommand(SendFile.TYPE);
		return cmd.getFile();
	}

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
