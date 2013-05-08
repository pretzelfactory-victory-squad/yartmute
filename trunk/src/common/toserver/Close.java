package common.toserver;

import java.io.BufferedWriter;


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
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}
	

}
