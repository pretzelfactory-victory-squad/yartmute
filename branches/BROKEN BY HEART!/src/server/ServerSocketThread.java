package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import common.CommandFactory;
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
			e.printStackTrace();
		}
	}
	public void run() {
		waitForCommand();
	}

	private void waitForCommand(){
		while(true){
			try {
				System.out.println("waiting for command");
				String line = reader.readLine();
				System.out.println("Command received: "+line);
				if(line == null){
					System.out.println("Client disconnected");	//TODO: close document properly
					s.close();
					return;
				}
				ServerCommand command = (ServerCommand) CommandFactory.getCommand(line);
				command.execute(writer);
			} catch (Exception e) {
				e.printStackTrace();
				//System.exit(-1);		//Det är lite väl dramatiskt att stänga av programmet när kommandot inte känns igen :)
			}
		}
	}
}
