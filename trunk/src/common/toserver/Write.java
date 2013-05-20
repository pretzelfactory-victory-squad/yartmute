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
	/**
	 * DRAGONS AHEAD! Modify the write command to be in sync.
	 * Must be tested. 
	 * @param list
	 */
	public void modify(List<Write> list){
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
		int fromIndex = (int)(toIndex- (serverVersion-versionBeforeMod-1));
		
		
		
		for(Write w: list.subList(fromIndex, toIndex)){
			String s = w.getText();
			int wLineStart = w.getLineStart();
			int wLineEnd = w.getLineEnd();
			int wSlotStart = w.getSlotStart();
			int wSlotEnd = w.getSlotEnd();
			int lineDiff = wLineStart-wLineEnd;
			
			if(wLineStart < modLineStart){
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
		this.setLineStart(modLineStart);
		this.setLineEnd(modLineEnd);
		this.setSlotStart(modSlotStart);
		this.setSlotEnd(modSlotEnd);
	}

	private void setSlotEnd(int slotEnd) {
		arg[3] = ""+slotEnd;
		
	}

	private void setSlotStart(int slotStart) {
		arg[2] = ""+slotStart;
		
	}

	private void setLineEnd(int lineEnd) {
		arg[1] = ""+lineEnd;
		
	}

	private void setLineStart(int lineStart) {
		arg[0] = ""+lineStart;
		
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
