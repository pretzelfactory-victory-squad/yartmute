package common.server;

import common.Command;

public class Write extends Command {

	public Write(String[] arg) {
		super(arg);
		type = "WRITE";
	}

}
