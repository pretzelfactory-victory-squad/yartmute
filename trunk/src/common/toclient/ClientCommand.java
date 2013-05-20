package common.toclient;

import common.Command;

/**
 * Abstract class for commands sent to the client
 */
public abstract class ClientCommand extends Command {

	public ClientCommand(String[] arg) {
		super(arg);
	}
	public ClientCommand(String arg) {
		super(arg);
	}

	public String getErrorMsg(){
		return null;
	}
	
	public boolean isSuccessful(){
		return getErrorMsg().equals("");
	}
}
