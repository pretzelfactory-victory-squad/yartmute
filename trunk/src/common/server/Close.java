package common.server;

import common.Command;

public class Close extends Command {

	public Close(String[] arg) {
		super(arg);
		type = "CLOSE";
	}

}
