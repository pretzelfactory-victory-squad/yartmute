package common.toserver;

import java.io.BufferedWriter;
import java.io.IOException;

import common.Command;
import common.Log;
import common.toclient.SendFile;

import server.ServerDoc;
import server.ServerDocHandler;
import server.exceptions.ServerExeptions;

public class Create extends ServerCommand {
	public static final String TYPE = "CRETE";
	public Create(String s) {
		super(s);	
	}
	public Create(String[] arg) {
		super(arg);
		type = TYPE;
	}
	@Override
	public void execute(BufferedWriter writer, ServerDoc doc) throws ServerExeptions {
		Command c = null;
		result = ServerDocHandler.instance.newDoc(getArg(0));
		int userId = result.addUser(writer);
		c = new SendFile(getArg(0), userId, result.getVerNbr(), result.getDoc());
		try {
			writer.write(c.toString());
			writer.flush();
		} catch (IOException e) {
			Log.error(e);
		}

	}

}
