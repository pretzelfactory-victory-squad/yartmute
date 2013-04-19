package common.server;

import common.Command;

public class List extends Command {

	public List(String[] arg) {
		super(arg);
		type = "LIST";
	}
	public List(){
		super(null);
		type = "LIST";	
	}

}
