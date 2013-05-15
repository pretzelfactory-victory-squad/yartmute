package common.toserver;

import java.io.BufferedWriter;
import java.util.List;

import common.Command;
import common.Command.CommandArgumentException;
import common.toclient.Update;

import server.ServerDoc;
import server.exceptions.OutOfSyncException;


public class Write extends ServerCommand {
	public static final String TYPE = "WRITE";

	public Write(int lineStart, int lineEnd, int slotStart, int slotEnd, String text, int userId, long version) throws CommandArgumentException{
		this(new String[]{""+lineStart, ""+lineEnd, ""+slotStart, ""+slotEnd, text, ""+userId, ""+version});
		if(lineEnd < lineStart){
			throw new CommandArgumentException("lineStart can't be bigger than lineEnd ("+lineStart+" > "+lineEnd+")");
		}else if(lineStart == lineEnd && slotEnd < slotStart){
			throw new CommandArgumentException("slotStart can't be bigger than slotEnd on single line updates ("+slotStart+" > "+slotEnd+")");
		}
	}
	
	public Write(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public int getLineStart(){
		return Integer.parseInt(arg[0]);
	}
	public int getLineEnd(){
		return Integer.parseInt(arg[1]);
	}
	public int getSlotStart(){
		return Integer.parseInt(arg[2]);
	}
	public int getSlotEnd(){
		return Integer.parseInt(arg[3]);
	}
	public String getText(){
		return arg[4];
	}
	public int getUserId(){
		return Integer.parseInt(arg[5]);
	}
	public long getVersion(){
		return Long.parseLong(arg[6]);
	}
	
	public void modify(List<Write> list){//inte färdig
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
		String[] updateArg = new String[7];
		for(int i=0; i<6; i++){
			updateArg[i]=arg[i];
		}
		updateArg[6] = ""+ (doc.getVerNbr());
		Command c = new Update(updateArg);
		doc.sendCmdToConnectedUsers(c);
	}

}
