package common.toserver;

import java.io.BufferedWriter;
import java.util.List;

import common.Command;
import common.toclient.Update;

import server.ServerDoc;
import server.exceptions.OutOfSyncException;


public class Write extends ServerCommand {
	public static final String TYPE = "WRITE";

	public Write(int lineStart, int lineEnd, int slotStart, int slotEnd, String text, long version){
		this(new String[]{""+lineStart, ""+lineEnd, ""+slotStart, ""+slotEnd, text, ""+version});
	}
	
	public Write(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public void modify(List<Write> list){
		//TODO: Implement commando adjustment. Increment version by 1.
	}
	@Override
	public void execute(BufferedWriter writer, ServerDoc doc) throws OutOfSyncException {
		doc.write(this);
		String[] updateArg = new String[6];
		for(int i=0; i<5; i++){
			updateArg[i]=arg[i];
		}
		updateArg[5] = ""+ (doc.getVerNbr());
		Command c = new Update(updateArg);
		doc.sendCmdToConnectedUsers(c);
	}

}
