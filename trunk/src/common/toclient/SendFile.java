package common.toclient;

/**
 * Command send from server to client as a response to a Open-command.
 */
public class SendFile extends ClientCommand {
	public static final String TYPE = "SFILE";

	public SendFile(String filename, int userId, long verNbr, String doc) {
		this(new String[]{filename, ""+userId, ""+verNbr, doc, ""});
	}
	
	public SendFile(String filename, int userId, long verNbr, String doc, String errorMsg) {
		this(new String[]{filename, ""+userId, ""+verNbr, doc, errorMsg});
	}
	
	public SendFile(String[] arg) {
		super(arg);
		this.type = TYPE;
	}
	public SendFile(String arg) {
		super(arg);
		this.type = TYPE;
	}

	public String getFile(){
		String contents = getArg(3);
		return contents;
	}
	
	public String getFileName(){
		return getArg(0);
	}
	
	public Long getVersion(){
		return Long.parseLong(getArg(2));
	}
	
	public String getErrorMsg(){
		return getArg(4);
	}
	
	public int getUserId(){
		return Integer.parseInt(getArg(1));
	}
}
