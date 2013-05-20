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
	
	/**
	 * Create a new socket writer
	 * @param os the outputstream for the socket
	 */
	public ClientSocketWriter(OutputStream os){
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.error(e);
		}
	}
	
	/**
	 * Send a command to the server
	 * @param command the command
	 * @throws SocketException
	 */
	public void sendCommand(ServerCommand command) throws SocketException{
		Log.debug("Sending cmd:  "+command);
		try {
			writer.write(command.toString());
			writer.flush();
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			Log.error(e);
		}
	}
	
	/**
	 * Request a list of editable files on the server
	 * @throws SocketException
	 */
	public void getFileList() throws SocketException{
		sendCommand(new GetFileList());
	}
	
	/**
	 * Request a file from the server
	 * @param file
	 * @throws SocketException
	 */
	public void openFile(String file) throws SocketException {
		sendCommand(new Open(file));
	}
}
