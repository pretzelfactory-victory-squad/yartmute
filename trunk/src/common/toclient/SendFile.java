package common.toclient;


public class SendFile extends ClientCommand {
	public static final String TYPE = "SFILE";

	public SendFile(String filename, long verNbr, String doc) {
		this(new String[]{filename, ""+verNbr, doc, ""});
	}
	
	public SendFile(String filename, long verNbr, String doc, String errorMsg) {
		this(new String[]{filename, ""+verNbr, doc, errorMsg});
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
		String filename = getArg(0);
		long version = Long.parseLong(getArg(1));
		String contents = getArg(2);
		return contents;
	}
	
	public String getErrorMsg(){
		return getArg(3);
	}
}
