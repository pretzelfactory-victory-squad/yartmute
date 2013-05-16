package common.toclient;


public class Update extends ClientCommand {
	public static final String TYPE = "UPDAT";

	public Update(int lineStart, int lineEnd, int slotStart, int slotEnd, String text, int userId, long version){
		this(new String[]{""+lineStart, ""+lineEnd, ""+slotStart, ""+slotEnd, text, ""+userId, ""+version});
	}
	
	public Update(String[] arg) {
		super(arg);
		type = TYPE;
	}
	
	public int getLineStart(){
		return Integer.valueOf(getArg(0));
	}
	
	public int getLineEnd(){
		return Integer.valueOf(getArg(1));
	}
	
	public int getSlotStart(){
		return Integer.valueOf(getArg(2));
	}

	public int getSlotEnd(){
		return Integer.valueOf(getArg(3));
	}
	
	public String getText(){
		return getArg(4);
	}
	
	public int getUserId(){
		return Integer.valueOf(getArg(5));
	}
	
	public long getVersion(){
		return Long.valueOf(getArg(6));
	}
}