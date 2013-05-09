package common.toserver;

import java.io.BufferedWriter;

import server.ServerDoc;


public class Close extends ServerCommand {
	public static final String TYPE = "CLOSE";

	public Close(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public Close(String s){
		super(s);
	}

	@Override
	public void execute(BufferedWriter writer, ServerDoc doc) {
		// TODO Auto-generated method stub
		
	}
	

}
