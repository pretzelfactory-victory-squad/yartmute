package common.toserver;

import java.io.BufferedWriter;


public class Write extends ServerCommand {
	public static final String TYPE = "WRITE";

	public Write(String[] arg) {
		super(arg);
		type = TYPE;
	}
	public Write(String s){
		super(s);
	}
	@Override
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}

}
