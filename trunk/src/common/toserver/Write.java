package common.toserver;

import java.io.BufferedWriter;


public class Write extends ServerCommand {
	public static final String TYPE = "WRITE";

	public Write(int lineStart, int lineEnd, int slotStart, int slotEnd, String text, long version){
		this(new String[]{""+lineStart, ""+lineEnd, ""+slotStart, ""+slotEnd, text, ""+version});
	}
	
	public Write(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	@Override
	public void execute(BufferedWriter writer) {
		// TODO Auto-generated method stub
		
	}

}
