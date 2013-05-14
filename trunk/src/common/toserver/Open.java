package common.toserver;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
		Command c = null;
		try{
			result = ServerDocHandler.instance.getDoc(getArg(0));
			result.addUser(writer);
			c = new SendFile(getArg(0), result.getVerNbr(), result.getDoc());
		} catch(FileNotFoundException e){
			c = new SendFile(getArg(0), -1, "", "File not found: "+getArg(0));
		}
		try {
			writer.write(c.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
