package common.server;

import common.Command;

public class Open extends Command {

	public Open(String[] arg) {
		super(arg);
		type = "OPEN";
	}
	
}
