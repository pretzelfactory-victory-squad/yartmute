package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import server.exceptions.OutOfSyncException;
import server.exceptions.ServerExeptions;
import common.CommandFactory;
import common.Log;
import common.CommandFactory.MalformedCommandException;
import common.toserver.Open;
import common.toserver.ServerCommand;

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
			Log.error(e);
		}
	}
	public void run() {
		waitForCommand();
	}

	private void waitForCommand(){
		while(true){
			try {
				//Log.debug("waiting for command");
				String line = reader.readLine();
				Log.debug("Command received: "+line);
				if(line == null){
					disconnect();
					return;
				}
				ServerCommand command = (ServerCommand) CommandFactory.getCommand(line);
				try{
					command.execute(writer, doc);
					if(command.getType().equals(Open.TYPE)){
						doc = command.result;
					}
				} catch (OutOfSyncException e){
					Log.error(e);
				}
			} catch(ServerExeptions e){
				Log.error(e);
			} catch (MalformedCommandException e) {
				Log.error(e);
			} catch (SocketException e) {
				disconnect();
				return;
			} catch (IOException e) {
				Log.error(e);
				disconnect();
				return;
			}
		}
	}
	
	private void disconnect() {
		if(doc!=null){
			doc.save();
			doc.removeUser(writer);
		}
		Log.debug("Client disconnected");
		
		try {
			s.close();
		} catch (IOException e) {
			Log.error(e);
		}
		
		Thread.currentThread().interrupt();
		return;
	}
}
