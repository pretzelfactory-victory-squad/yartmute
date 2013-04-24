package common.toserver;

import java.io.BufferedWriter;


public class Write extends ServerCommand {

	public Write(String[] arg) {
		super(arg);
		type = "WRITE";
	}
	public Write(String s){
		super(s);
	}
	@Override
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}

}
