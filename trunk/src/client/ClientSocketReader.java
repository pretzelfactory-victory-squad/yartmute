package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import common.Command;
import common.CommandFactory;
import common.toserver.GetFileList;

public class ClientSocketReader{

	private boolean isConnected;
	private BufferedReader reader;
	private Client client;
	private Thread thread;
	private List<Command> commands = new ArrayList<Command>();

	public ClientSocketReader(final Client client, Socket socket){
		try {
			this.client = client;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
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
		});
		thread.start();
	}
	
	public void waitForUpdate() throws IOException{
		String line = reader.readLine();
		if(line == null){
			isConnected = false;
			return;
		}
		
		try {
			commands.add(CommandFactory.getCommand(line));
			notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String[] waitForFileList() {
		String[] empty = {};
		try {
			boolean wait = true;
			while(wait){
				for(Command c : commands){
					if(c instanceof List){
						wait = false;
						return ((GetFileList)c).getFileList();
					}
				}
				wait();
			}
			return empty;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return empty;
		}
	}
}
