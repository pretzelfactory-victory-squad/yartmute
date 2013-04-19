package client;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ClientSocketWriter {
	private BufferedWriter writer;
	public ClientSocketWriter(OutputStream os){
		writer = new BufferedWriter(new OutputStreamWriter(os));
	}
	public void getFileList(){
		sendCommand();
	}
	public void sendCommand(){
		
	}
}
