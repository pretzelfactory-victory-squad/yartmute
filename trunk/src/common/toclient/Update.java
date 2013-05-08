package common.toclient;


public class Update extends ClientCommand {
	public static final String TYPE = "UPDAT";

	public Update(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public Update(String s){
		super(s);
	}

}
