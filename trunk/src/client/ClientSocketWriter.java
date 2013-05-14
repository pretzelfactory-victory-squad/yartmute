package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import common.Log;
import common.toserver.GetFileList;
import common.toserver.Open;
import common.toserver.ServerCommand;

public class ClientSocketWriter {
	private BufferedWriter writer;
	public ClientSocketWriter(OutputStream os){
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCommand(ServerCommand command) throws SocketException{
		Log.debug("Sending cmd:  "+command);
		try {
			writer.write(command.toString());
			writer.flush();
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getFileList() throws SocketException{
		sendCommand(new GetFileList());
	}
	
	public void openFile(String file) throws SocketException {
		sendCommand(new Open(file));
	}
}
