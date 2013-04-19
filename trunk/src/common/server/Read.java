package common.server;

import common.Command;

public class Read extends Command {

	public Read(String[] arg) {
		super(arg);
		type = "READ";
	}

}
