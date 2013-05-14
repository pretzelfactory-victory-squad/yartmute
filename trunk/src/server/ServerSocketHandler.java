package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler {
	private ServerSocket  serverSocket = null;
	
	public ServerSocketHandler (int port){
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("server started on port "+serverSocket.getLocalPort());
			waitForClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForClient(){
		while(true){
			//System.out.println("waiting for connection");
			Socket socket = null;
			try{
				socket = serverSocket.accept();
				System.out.println("Client connected: "+socket.getInetAddress());
				
				Thread t = new Thread(new ServerSocketThread(socket), "ServerSocketThread");
				t.start();
				
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		}
	}
}
