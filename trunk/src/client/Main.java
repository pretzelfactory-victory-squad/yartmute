package client;

import java.net.Socket;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client();
		new ClientGUI(client);
	}

}
