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
	
	public void modify(List<Write> list){//inte f�rdig
		//TODO: Implement commando adjustment. Increment version by 1.
		if(list.size() == 0){
			return;
		}
		/*
		String[] modifiedWrite = new String[6]; 
		modifiedWrite[5] = list.get(list.size()-1).getArg(5);
		int modLineStart = Integer.valueOf(modifiedWrite[0]);
		int modLineEnd = Integer.valueOf(modifiedWrite[1]);
		int modSlotStart = Integer.valueOf(modifiedWrite[2]);
		int modSlotEnd = Integer.valueOf(modifiedWrite[3]);
		for(Write w: list){
			if(modLineStart < Integer.valueOf(w.getArg(0))){
				int lineBreaks = w.getArg(4).split("\n").length;
			}
		}*/
		
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
