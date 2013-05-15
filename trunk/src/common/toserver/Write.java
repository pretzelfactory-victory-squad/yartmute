package common.toserver;

import java.io.BufferedWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	private void setVersion(long l) {
		arg[6] = ""+l;
	}
	
	public void modify(List<Write> list){//inte färdig
		long versionBeforeMod = getVersion();
		setVersion(versionBeforeMod+1);
		
		if(list.size() == 0){
			return;
		}
		
		long serverVersion = list.get(list.size()-1).getVersion();
		setVersion(serverVersion+1);
		
		if(versionBeforeMod == serverVersion){
			return;
		}
		
		int modLineStart = this.getLineStart();
		int modLineEnd = this.getLineEnd();
		int modSlotStart = this.getSlotStart();
		int modSlotEnd = this.getSlotEnd();
		
		int toIndex = list.size()-1;
		int fromIndex = (int)(toIndex- (serverVersion-versionBeforeMod));
		
		
		
		for(Write w: list.subList(fromIndex, toIndex)){
			String s = w.getArg(4);
			int wLineStart = Integer.valueOf(w.getArg(0));
			int wLineEnd = Integer.valueOf(w.getArg(1));
			int wSlotStart = Integer.valueOf(w.getArg(2));
			int wSlotEnd = Integer.valueOf(w.getArg(3));
			int lineDiff = wLineStart-wLineEnd;
			
			if(wLineStart <= modLineStart){
				int lines = StringUtils.splitPreserveAllTokens(s, "\n").length; 
				if(lines != 0 && lineDiff == 0){ 	
						modLineStart = modLineStart + lines;
						modLineEnd = modLineEnd + lines;
				}else{
						modLineStart = modLineStart + lineDiff;
						modLineEnd = modLineEnd +lineDiff;
					}
				if(wLineEnd == modLineEnd && wLineStart != modLineStart){
					modSlotStart = modSlotStart + wSlotEnd;
					modSlotEnd = modSlotEnd + wSlotEnd;
				}
			}else if(wLineStart == modLineStart && modSlotStart > wSlotStart && lineDiff == 0){
				int sLength = s.length();
				int slotDiff = wSlotStart - wSlotEnd;
				if(slotDiff != 0){
					modSlotStart = modSlotStart + slotDiff;
					modSlotEnd = modSlotEnd + slotDiff;
				}else{
					modSlotStart = modSlotStart + sLength;
					modSlotEnd = modSlotEnd + sLength;
				}
			}
		}
		arg[0] = ""+modLineStart;
		arg[1] = ""+modLineEnd;
		arg[2] = ""+modSlotStart;
		arg[3] = ""+modSlotEnd;
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
