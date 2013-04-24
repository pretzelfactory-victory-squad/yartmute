package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import common.client.ClientCommand;
import common.client.GetFileList;

public class ClientSocketWriter {
	private BufferedWriter writer;
	public ClientSocketWriter(OutputStream os){
		writer = new BufferedWriter(new OutputStreamWriter(os));
	}
	public void getFileList(){
		sendCommand(new GetFileList());
	}
	public void sendCommand(ClientCommand command){
		try {
			writer.write(command.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
