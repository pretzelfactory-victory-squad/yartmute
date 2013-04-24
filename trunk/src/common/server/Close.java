package common.server;


public class Close extends ServerCommand {

	public Close(String[] arg) {
		super(arg);
		type = "CLOSE";
	}

}
