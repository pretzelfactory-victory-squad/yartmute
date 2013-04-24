package common.server;


public class Write extends ServerCommand {

	public Write(String[] arg) {
		super(arg);
		type = "WRITE";
	}

}
