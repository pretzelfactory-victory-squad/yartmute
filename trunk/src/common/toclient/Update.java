package common.toclient;


public class Update extends ClientCommand {

	public Update(String[] arg) {
		super(arg);
		type = "UPDATE";
	}

}
