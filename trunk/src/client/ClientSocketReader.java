package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ClientSocketReader extends Thread{

	private boolean isConnected;
	private BufferedReader reader;
	private Client client;

	public ClientSocketReader(Client client, Socket socket){
		try {
			this.client = client;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
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
	
	public void waitForUpdate() throws IOException{
		String line = reader.readLine();
		if(line == null){
			isConnected = false;
			return;
		}
	}
}
