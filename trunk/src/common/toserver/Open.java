package common.toserver;

import java.io.BufferedWriter;


public class Open extends ServerCommand {

	public Open(String[] arg) {
		super(arg);
		type = "OPEN";
	}
	
	public Open(String s){
		super(s);
	}

	@Override
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}
	
	
}
