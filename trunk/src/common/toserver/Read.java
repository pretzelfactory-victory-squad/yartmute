package common.toserver;

import java.io.BufferedWriter;

import server.ServerDoc;


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
	public void execute(BufferedWriter writer, ServerDoc doc) {
		// TODO Auto-generated method stub
		
	}

}
