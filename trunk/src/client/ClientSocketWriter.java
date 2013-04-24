package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import common.toserver.GetFileList;
import common.toserver.Open;
import common.toserver.ServerCommand;

public class ClientSocketWriter {
	private BufferedWriter writer;
	public ClientSocketWriter(OutputStream os){
		writer = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	public void sendCommand(ServerCommand command){
		try {
			writer.write(command.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getFileList(){
		sendCommand(new GetFileList());
	}
	
	public void openFile(String file) {
		sendCommand(new Open(file));
	}
}
