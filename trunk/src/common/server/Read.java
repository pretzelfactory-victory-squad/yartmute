package common.server;


public class Read extends ServerCommand {

	public Read(String[] arg) {
		super(arg);
		type = "READ";
	}

}
