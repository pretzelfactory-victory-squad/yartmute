package common.toserver;

import java.io.BufferedWriter;
import java.io.IOException;

import common.Command;
import common.toclient.SendFile;

import server.ServerDoc;
import server.ServerDocHandler;


public class Open extends ServerCommand {
	public static final String TYPE = "OPEN_";

	public Open(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public Open(String s){
		super(s);
		type = TYPE;
	}

	@Override
	public void execute(BufferedWriter writer, final ServerDoc doc) { // doc might be null, always use result
		result = ServerDocHandler.instance.getDoc(getArg(0));
		result.addUser(writer);
		String[] s = new String[3];
		s[0] = getArg(0);
		s[1] = String.valueOf(result.getVerNbr());
		s[2] = result.getDoc();
		Command c = new SendFile(s);
		try {
			writer.write(c.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
