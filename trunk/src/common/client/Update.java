package common.client;

import common.Command;

public class Update extends Command {

	public Update(String[] arg) {
		super(arg);
		type = "UPDATE";
	}

}
