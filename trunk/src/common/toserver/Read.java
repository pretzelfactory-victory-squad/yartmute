package common.toserver;

import java.io.BufferedWriter;


public class Read extends ServerCommand {
	public static final String TYPE = "READ_";

	public Read(String[] arg) {
		super(arg);
		type = TYPE;
	}
	public Read(String s){
		super(s);
	}

	@Override
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}

}
